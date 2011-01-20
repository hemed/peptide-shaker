package eu.isas.peptideshaker;

import com.compomics.util.experiment.MsExperiment;
import com.compomics.util.experiment.ProteomicAnalysis;
import com.compomics.util.experiment.biology.Sample;
import com.compomics.util.experiment.identification.Identification;
import com.compomics.util.experiment.identification.IdentificationMethod;
import com.compomics.util.experiment.identification.PeptideAssumption;
import com.compomics.util.experiment.identification.identifications.Ms2Identification;
import com.compomics.util.experiment.identification.matches.PeptideMatch;
import com.compomics.util.experiment.identification.matches.ProteinMatch;
import com.compomics.util.experiment.identification.matches.SpectrumMatch;
import com.compomics.util.experiment.massspectrometry.SpectrumCollection;
import eu.isas.peptideshaker.fdrestimation.InputMap;
import eu.isas.peptideshaker.fdrestimation.PeptideSpecificMap;
import eu.isas.peptideshaker.fdrestimation.PsmSpecificMap;
import eu.isas.peptideshaker.fdrestimation.TargetDecoyMap;
import eu.isas.peptideshaker.gui.WaitingDialog;
import eu.isas.peptideshaker.fileimport.IdFilter;
import eu.isas.peptideshaker.fileimport.IdImporter;
import eu.isas.peptideshaker.fileimport.SpectrumImporter;
import eu.isas.peptideshaker.myparameters.PSMaps;
import eu.isas.peptideshaker.myparameters.PSParameter;
import eu.isas.peptideshaker.preferences.IdentificationPreferences;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * This class will be responsible for the identification import and the associated calculations
 * @author Marc
 */
public class PeptideShaker {

    /**
     * The experiment conducted
     */
    private MsExperiment experiment;
    /**
     * The sample analyzed
     */
    private Sample sample;
    /**
     * The replicate number
     */
    private int replicateNumber;
    /**
     * The psm map
     */
    private PsmSpecificMap psmMap;
    /**
     * The peptide map
     */
    private PeptideSpecificMap peptideMap;
    /**
     * The protein map
     */
    private TargetDecoyMap proteinMap;
    /**
     * The id importer will import and process the identifications
     */
    private IdImporter idImporter = null;
    /**
     * The spectrum importer will import spectra
     */
    private SpectrumImporter spectrumImporter;
    /**
     * Boolean indicating whether the processing of identifications is finished
     */
    private boolean idProcessingFinished = false;
    /**
     * The queuing objects
     */
    private ArrayList<Object> queue = new ArrayList<Object>();

    /**
     * constructor without mass specification. Calculation will be done on new maps which will be retrieved as compomics utilities parameters.
     * @param experiment        The experiment conducted
     * @param sample            The sample analyzed
     * @param replicateNumber   The replicate number
     */
    public PeptideShaker(MsExperiment experiment, Sample sample, int replicateNumber) {
        this.experiment = experiment;
        this.sample = sample;
        this.replicateNumber = replicateNumber;
        psmMap = new PsmSpecificMap(experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber).getSpectrumCollection());
        peptideMap = new PeptideSpecificMap();
        proteinMap = new TargetDecoyMap("protein");
    }

    /**
     * Constructor with map specifications
     * @param experiment        The experiment conducted
     * @param sample            The sample analyzed
     * @param replicateNumber   The replicate number
     * @param psMaps           the peptide shaker maps
     */
    public PeptideShaker(MsExperiment experiment, Sample sample, int replicateNumber, PSMaps psMaps) {
        this.experiment = experiment;
        this.sample = sample;
        this.replicateNumber = replicateNumber;
        this.psmMap = psMaps.getPsmSpecificMap();
        this.peptideMap = psMaps.getPeptideSpecificMap();
        this.proteinMap = psMaps.getProteinMap();
    }

    /**
     * Method used to import identification from identification result files
     * @param waitingDialog     A dialog to display the feedback
     * @param idFilter          The identification filter to use
     * @param idFiles           The files to import
     */
    public void importIdentifications(WaitingDialog waitingDialog, IdFilter idFilter, ArrayList<File> idFiles) {
        ProteomicAnalysis analysis = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber);
        Ms2Identification identification = new Ms2Identification();
        analysis.addIdentificationResults(IdentificationMethod.MS2_IDENTIFICATION, identification);
        idImporter = new IdImporter(this, waitingDialog, experiment, sample, replicateNumber, idFilter);
        idImporter.importFiles(idFiles);
    }

    /**
     * Method used to import spectra from files
     * @param waitingDialog     A dialog to display feedback to the user
     * @param identified        A boolean indicating whether only identified spectra should be loaded
     * @param spectrumFiles     The files to import
     */
    public void importSpectra(WaitingDialog waitingDialog, boolean identified, ArrayList<File> spectrumFiles) {
        ProteomicAnalysis analysis = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber);
        spectrumImporter = new SpectrumImporter(this, analysis, identified);
        spectrumImporter.importSpectra(waitingDialog, spectrumFiles);
    }

    /**
     * Add an object to the queue waiting for the identification import to complete
     * @param object    the object to add in the queue
     */
    public void queue(Object object) {
            queue.add(object);
    }

    public boolean needQueue() {
        return (idImporter != null && !idProcessingFinished);
    }

    public synchronized void emptyQueue() {
        for (Object object : queue) {
            object.notify();
        }
    }

    public void setRunFinished(WaitingDialog waitingDialog) {
        if (idImporter != null) {
            if (!idProcessingFinished) {
                return;
            }
        }
        if (spectrumImporter != null) {
            if (!spectrumImporter.isRunFinished()) {
                return;
            }
        }
        waitingDialog.setRunFinished();
    }

    /**
     * Method used to import sequences from a fasta file
     * @param waitingDialog     A dialog to display feedback to the user
     * @param file              the file to import
     */
    public void importFasta(WaitingDialog waitingDialog, File file) {
        //@TODO implement
    }

    /**
     * Method for processing of results from utilities data (no file). From ms_lims for instance.
     * @param waitingDialog     A dialog to display the feedback
     */
    public void processIdentifications(WaitingDialog waitingDialog) {
        IdImporter idImporter = new IdImporter(this, waitingDialog, experiment, sample, replicateNumber);
        idImporter.importIdentifications();
    }

    /**
     * This method processes the identifications and fills the peptide shaker maps
     *
     * @param inputMap          The input map
     * @param waitingDialog     A dialog to display the feedback
     */
    public void processIdentifications(InputMap inputMap, WaitingDialog waitingDialog) {
        if (inputMap.isMultipleSearchEngines()) {
            inputMap.computeProbabilities(waitingDialog);
        }
        waitingDialog.appendReport("Computing spectrum probabilities.");
        fillPsmMap(inputMap);
        psmMap.cure(waitingDialog);
        psmMap.estimateProbabilities(waitingDialog);
        attachSpectrumProbabilities();
        waitingDialog.appendReport("Computing peptide probabilities.");
        fillPeptideMaps();
        peptideMap.cure(waitingDialog);
        peptideMap.estimateProbabilities(waitingDialog);
        attachPeptideProbabilities();
        waitingDialog.appendReport("Computing protein probabilities.");
        fillProteinMap();
        proteinMap.estimateProbabilities(waitingDialog);
        attachProteinProbabilities();
        waitingDialog.appendReport("Identification processing completed.");
        Identification identification = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber).getIdentification(IdentificationMethod.MS2_IDENTIFICATION);
        identification.addUrParam(new PSMaps(proteinMap, psmMap, peptideMap));
        idProcessingFinished = true;
        setRunFinished(waitingDialog);
    }

    /**
     * This method will estimate for each map the score thresholds, FDR and FNR when possible
     */
    public void estimateThresholds(IdentificationPreferences identificationPreferences) {
        boolean probabilistic = identificationPreferences.useProbabilisticFDR();
        proteinMap.setProbabilistic(probabilistic);
        peptideMap.setProbabilistic(probabilistic);
        psmMap.setProbabilistic(probabilistic);

        proteinMap.getResults(identificationPreferences.getProteinThreshold());
        peptideMap.getResults(identificationPreferences.getPeptideThreshold());
        psmMap.getResults(identificationPreferences.getPsmThreshold());
    }

    /**
     * This method will flag validated identifications
     */
    public void validateIdentifications() {
        Identification identification = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber).getIdentification(IdentificationMethod.MS2_IDENTIFICATION);
        PSParameter psParameter = new PSParameter();

        double proteinThreshold = proteinMap.getScoreLimit();
        for (ProteinMatch proteinMatch : identification.getProteinIdentification().values()) {
            psParameter = (PSParameter) proteinMatch.getUrParam(psParameter);
            if (psParameter.getProteinProbabilityScore() < proteinThreshold) {
                psParameter.setValidated(true);
            }
        }

        double peptideThreshold;
        for (PeptideMatch peptideMatch : identification.getPeptideIdentification().values()) {
            peptideThreshold = peptideMap.getScoreLimit(peptideMatch);
            psParameter = (PSParameter) peptideMatch.getUrParam(psParameter);
            if (psParameter.getPeptideProbabilityScore() < peptideThreshold) {
                psParameter.setValidated(true);
            }
        }

        double psmThreshold;
        for (SpectrumMatch spectrumMatch : identification.getSpectrumIdentification().values()) {
            psmThreshold = psmMap.getScoreLimit(spectrumMatch);
            psParameter = (PSParameter) spectrumMatch.getUrParam(psParameter);
            if (psParameter.getSpectrumProbabilityScore() < psmThreshold) {
                psParameter.setValidated(true);
            }
        }
    }

    /**
     * Fills the psm specific map
     *
     * @param inputMap       The input map
     */
    private void fillPsmMap(InputMap inputMap) {
        Identification identification = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber).getIdentification(IdentificationMethod.MS2_IDENTIFICATION);
        HashMap<String, Double> identifications;
        HashMap<Double, PeptideAssumption> peptideAssumptions;
        PSParameter psParameter;
        PeptideAssumption peptideAssumption;
        if (inputMap.isMultipleSearchEngines()) {
            for (SpectrumMatch spectrumMatch : identification.getSpectrumIdentification().values()) {
                psParameter = new PSParameter();
                identifications = new HashMap<String, Double>();
                peptideAssumptions = new HashMap<Double, PeptideAssumption>();
                String id;
                double p, pScore = 1;
                for (int searchEngine : spectrumMatch.getAdvocates()) {
                    peptideAssumption = spectrumMatch.getFirstHit(searchEngine);
                    p = inputMap.getProbability(searchEngine, peptideAssumption.getEValue());
                    pScore = pScore * p;
                    id = peptideAssumption.getPeptide().getIndex();
                    if (identifications.containsKey(id)) {
                        p = identifications.get(id) * p;
                        identifications.put(id, p);
                        peptideAssumptions.put(p, peptideAssumption);
                    } else {
                        identifications.put(id, p);
                        peptideAssumptions.put(p, peptideAssumption);
                    }
                }
                double pMin = Collections.min(identifications.values());
                psParameter.setSpectrumProbabilityScore(pScore);
                spectrumMatch.addUrParam(psParameter);
                spectrumMatch.setBestAssumption(peptideAssumptions.get(pMin));
                psmMap.addPoint(pScore, spectrumMatch);
            }
        } else {
            double eValue;
            for (SpectrumMatch spectrumMatch : identification.getSpectrumIdentification().values()) {
                psParameter = new PSParameter();
                for (int searchEngine : spectrumMatch.getAdvocates()) {
                    peptideAssumption = spectrumMatch.getFirstHit(searchEngine);
                    eValue = peptideAssumption.getEValue();
                    psParameter.setSpectrumProbabilityScore(eValue);
                    spectrumMatch.setBestAssumption(peptideAssumption);
                    psmMap.addPoint(eValue, spectrumMatch);
                }
                spectrumMatch.addUrParam(psParameter);
            }
        }
    }

    /**
     * Attaches the spectrum posterior error probabilities to the spectrum matches
     */
    private void attachSpectrumProbabilities() {
        Identification identification = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber).getIdentification(IdentificationMethod.MS2_IDENTIFICATION);
        PSParameter psParameter = new PSParameter();
        for (SpectrumMatch spectrumMatch : identification.getSpectrumIdentification().values()) {
            psParameter = (PSParameter) spectrumMatch.getUrParam(psParameter);
            psParameter.setSpectrumProbability(psmMap.getProbability(spectrumMatch, psParameter.getSpectrumProbabilityScore()));
        }
    }

    /**
     * Fills the peptide specific map
     */
    private void fillPeptideMaps() {
        Identification identification = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber).getIdentification(IdentificationMethod.MS2_IDENTIFICATION);
        double probaScore;
        PSParameter psParameter = new PSParameter();
        for (PeptideMatch peptideMatch : identification.getPeptideIdentification().values()) {
            probaScore = 1;
            for (SpectrumMatch spectrumMatch : peptideMatch.getSpectrumMatches().values()) {
                if (spectrumMatch.getBestAssumption().getPeptide().isSameAs(peptideMatch.getTheoreticPeptide())) {
                    psParameter = (PSParameter) spectrumMatch.getUrParam(psParameter);
                    probaScore = probaScore * psParameter.getSpectrumProbability();
                }
            }
            psParameter = new PSParameter();
            psParameter.setPeptideProbabilityScore(probaScore);
            peptideMatch.addUrParam(psParameter);
            peptideMap.addPoint(probaScore, peptideMatch);
        }
    }

    /**
     * Attaches the peptide posterior error probabilities to the peptide matches
     */
    private void attachPeptideProbabilities() {
        Identification identification = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber).getIdentification(IdentificationMethod.MS2_IDENTIFICATION);
        PSParameter psParameter = new PSParameter();
        for (PeptideMatch peptideMatch : identification.getPeptideIdentification().values()) {
            psParameter = (PSParameter) peptideMatch.getUrParam(psParameter);
            psParameter.setPeptideProbability(peptideMap.getProbability(peptideMatch, psParameter.getPeptideProbabilityScore()));
        }
    }

    /**
     * fills the protein map
     */
    private void fillProteinMap() {
        Identification identification = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber).getIdentification(IdentificationMethod.MS2_IDENTIFICATION);
        double probaScore;
        PSParameter psParameter = new PSParameter();
        for (ProteinMatch proteinMatch : identification.getProteinIdentification().values()) {
            probaScore = 1;
            for (PeptideMatch peptideMatch : proteinMatch.getPeptideMatches().values()) {
                if (peptideMatch.getTheoreticPeptide().getParentProteins().size() == 1) {
                    psParameter = (PSParameter) peptideMatch.getUrParam(psParameter);
                    probaScore = probaScore * psParameter.getPeptideProbability();
                }
            }
            psParameter = new PSParameter();
            psParameter.setProteinProbabilityScore(probaScore);
            proteinMatch.addUrParam(psParameter);
            proteinMap.put(probaScore, proteinMatch.isDecoy());
        }
    }

    /**
     * Attaches the protein posterior error probability to the protein matches
     */
    private void attachProteinProbabilities() {
        Identification identification = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber).getIdentification(IdentificationMethod.MS2_IDENTIFICATION);
        PSParameter psParameter = new PSParameter();
        for (ProteinMatch proteinMatch : identification.getProteinIdentification().values()) {
            psParameter = (PSParameter) proteinMatch.getUrParam(psParameter);
            psParameter.setProteinProbability(proteinMap.getProbability(psParameter.getProteinProbabilityScore()));
        }
    }
}
