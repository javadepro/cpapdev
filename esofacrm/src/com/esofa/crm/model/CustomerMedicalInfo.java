package com.esofa.crm.model;

import java.io.Serializable;
import java.util.Date;

import com.esofa.crm.annotation.customer.CustomerTabIdentifier;
import com.esofa.crm.annotation.customer.CustomerTabNameE;
import com.esofa.crm.refdata.model.CpapDiagnosis;
import com.esofa.crm.refdata.model.DentalClinic;
import com.esofa.crm.refdata.model.Dentist;
import com.esofa.crm.refdata.model.FamilyDoctor;
import com.esofa.crm.refdata.model.SleepClinic;
import com.esofa.crm.refdata.model.SleepDoctor;
import com.esofa.crm.validator.customer.CustomerReferredByCheck;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
@CustomerReferredByCheck
public class CustomerMedicalInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	
	@Id
	Long id;
	
	@Parent
	private Key<Customer> customer;
	
	/** Medical Information **/
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private Key<FamilyDoctor> familyDoctor;
	
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private Key<SleepDoctor> sleepDoctor;

	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private Key<SleepClinic> clinic;

	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private Key<Dentist> dentist;
	
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private Key<DentalClinic> dentalClinic;
	
	
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private String referredBy;
	
	//date to indicate when the referredBy was used and hopefully the user
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private Date referredDate;
	
	/** Personal Info **/
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean isSmoker;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private String dentures;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean isSleepsWithDentures;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean hasPet;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean hasSleepPartner;
	
	//per month
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private String travelFreq;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean hasFacialHair;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private String occupation;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private String sleepPosition;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private String medicationList;
	
	/** Therapeutic Info **/
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private Key<CpapDiagnosis> diagnosis;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private String dateOfDiagnosticStudy;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private Date dateOfCpapTitration;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private String ahi;
	/** 40-100 step 5 **/
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private String o2Percent;
	/** 40-100 step 5 **/
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private String longestApnea;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean existingCpapPatient;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private Date since;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private String wakeUpUrinationEvents;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private String approxTimeOfSleepOnset;
	
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private String bedTimeHour;
	
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean hasSnore;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean hasTossAndTurn;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean hasDryMouth;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean isShiftWorker;

	/** CPAP Information **/
	/** 4-25 step 1 in (cmH2O)**/
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.CPAP)
	private String cpapPressure="";
	
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.CPAP)
	private boolean hasRamp;
	/** 20 - 40 **/
	
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.CPAP)
	private String rampMins="";
	/** 4 - 25 **/	
	
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.CPAP)
	private String ramp;
	
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.CPAP)
	private boolean hasEprCFlex;
	
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.CPAP)
	private String eprCFlexDesc;
	
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.CPAP)
	private Key<Product> currentCpapMachine;
	
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.CPAP)
	private String currentCpapMachineSerial;
	
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.CPAP)
	private String currentCpapMachineHumidifierSerial;
	
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.CPAP)
	private Key<Product> replacementCpapMachine;
	
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.CPAP)
	private String replacementCpapMachineSerial;
	
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.CPAP)
	private String replacementCpapMachineHumidifierSerial;
	
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.CPAP)
	private Date cpapPurchaseDate;
	
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.CPAP)
	private Key<Product> currentMask;
	
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.CPAP)
	private Date maskPurchaseDate;
	
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.CPAP)
	private String cpapPurchaseNotes;
	
	/** Medical History **/
	/**
	 * Yes-No-Comment requires flag and comment to be named exactly as "<field>" and "<field>Flag"
	 */
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean allergiesFlag;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private String allergies;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean hypertensionFlag;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private String hypertension;
	
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean sinusProblemsFlag;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private String sinusProblems;
	
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean cardiacHxFlag;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private String cardiacHx;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean diabetesFlag;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private String diabetes;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean chronicFaitqueFlag;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private String chronicFaitque;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean asthmaFlag;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private String asthma;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean thyroidHxFlag;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private String thyroidHx;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean epitaxisFlag;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private String epitaxis;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean gerdFlag;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private String gerd;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean bruxismFlag;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private String bruxism;
	
	/**
	 * additional sleepmed properties
	 */
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean highBloodPressureFlag;	
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean atrialFibrillationFlag;	
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean congestedHeartFailureFlag;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean halitosisFlag;
	
	
	
	/** SP1 addition **/
	@Deprecated	
	private boolean cerebrospinalLeaksFlag;
	@Deprecated
	private String cerebrospinalLeaks;
	@Deprecated
	private boolean severeBullousLungDiseaseFlag;
	@Deprecated
	private String severeBullousLungDisease;
	@Deprecated
	private boolean pathologicalLowBloodPressureFlag;
	@Deprecated
	private String pathologicalLowBloodPressure;
	@Deprecated
	private boolean abnormalCribriformPlateFlag;
	@Deprecated
	private String abnormalCribriformPlate;
	@Deprecated
	private boolean depressionFlag;
	@Deprecated
	private String depression;
	@Deprecated
	private boolean pneumothoraxFlag;
	@Deprecated
	private String pneumothorax;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean claustrophobiaFlag;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private String claustrophobia;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean acidRefluxSyndromeFlag;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private String acidRefluxSyndrome;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean droolingFlag;
	
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean nightmaresFlag;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean copdFlag;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private String copd;
	
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean morningHeadaches;
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private boolean sleepingPills;
	
	/** Other **/
	@CustomerTabIdentifier(usedIn=CustomerTabNameE.MEDICAL)
	private String specialMedicalNote;
	
	public CustomerMedicalInfo() {
	}
	
	public CustomerMedicalInfo (Key<Customer> customer) {
		this.customer = customer;
	}
	
	/** Getter Setter **/
	public Key<FamilyDoctor> getFamilyDoctor() {
		return familyDoctor;
	}

	public void setFamilyDoctor(Key<FamilyDoctor> familyDoctor) {
		this.familyDoctor = familyDoctor;
	}

	public Key<SleepDoctor> getSleepDoctor() {
		return sleepDoctor;
	}

	public void setSleepDoctor(Key<SleepDoctor> sleepDoctor) {
		this.sleepDoctor = sleepDoctor;
	}

	public Key<SleepClinic> getClinic() {
		return clinic;
	}

	public void setClinic(Key<SleepClinic> clinic) {
		this.clinic = clinic;
	}

	public Key<Dentist> getDentist() {
		return dentist;
	}

	public void setDentist(Key<Dentist> dentist) {
		this.dentist = dentist;
	}

	public Key<DentalClinic> getDentalClinic() {
		return dentalClinic;
	}

	public void setDentalClinic(Key<DentalClinic> dentalClinic) {
		this.dentalClinic = dentalClinic;
	}

	public Key<CpapDiagnosis> getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(Key<CpapDiagnosis> diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getDateOfDiagnosticStudy() {
		return dateOfDiagnosticStudy;
	}

	public void setDateOfDiagnosticStudy(String dateOfDiagnosticStudy) {
		this.dateOfDiagnosticStudy = dateOfDiagnosticStudy;
	}

	public Date getDateOfCpapTitration() {
		return dateOfCpapTitration;
	}

	public void setDateOfCpapTitration(Date dateOfCpapTitration) {
		this.dateOfCpapTitration = dateOfCpapTitration;
	}

	public String getAhi() {
		return ahi;
	}

	public void setAhi(String ahi) {
		this.ahi = ahi;
	}

	public String getO2Percent() {
		return o2Percent;
	}

	public void setO2Percent(String o2Percent) {
		this.o2Percent = o2Percent;
	}

	public String getLongestApnea() {
		return longestApnea;
	}

	public void setLongestApnea(String longestApnea) {
		this.longestApnea = longestApnea;
	}

	public boolean getExistingCpapPatient() {
		return existingCpapPatient;
	}

	public void setExistingCpapPatient(boolean existingCpapPatient) {
		this.existingCpapPatient = existingCpapPatient;
	}

	public Date getSince() {
		return since;
	}

	public void setSince(Date since) {
		this.since = since;
	}

	public String getWakeUpUrinationEvents() {
		return wakeUpUrinationEvents;
	}

	public void setWakeUpUrinationEvents(String wakeUpUrinationEvents) {
		this.wakeUpUrinationEvents = wakeUpUrinationEvents;
	}

	public String getApproxTimeOfSleepOnset() {
		return approxTimeOfSleepOnset;
	}

	public void setApproxTimeOfSleepOnset(String approxTimeOfSleepOnset) {
		this.approxTimeOfSleepOnset = approxTimeOfSleepOnset;
	}
	
	public String getBedTimeHour() {
		return bedTimeHour;
	}

	public void setBedTimeHour(String bedTimeHour) {
		this.bedTimeHour = bedTimeHour;
	}	

	public boolean getHasSnore() {
		return hasSnore;
	}

	public void setHasSnore(boolean hasSnore) {
		this.hasSnore = hasSnore;
	}

	public boolean getHasTossAndTurn() {
		return hasTossAndTurn;
	}

	public void setHasTossAndTurn(boolean hasTossAndTurn) {
		this.hasTossAndTurn = hasTossAndTurn;
	}

	public boolean getHasDryMouth() {
		return hasDryMouth;
	}

	public void setHasDryMouth(boolean hasDryMouth) {
		this.hasDryMouth = hasDryMouth;
	}

	public boolean getIsShiftWorker() {
		return isShiftWorker;
	}

	public void setIsShiftWorker(boolean isShiftWorker) {
		this.isShiftWorker = isShiftWorker;
	}

	public String getCpapPressure() {
		if(cpapPressure==null){
			return "";
		}
		return cpapPressure;
	}

	public void setCpapPressure(String cpapPressure) {
		this.cpapPressure = cpapPressure;
	}

	public boolean getHasRamp() {
		return hasRamp;
	}

	public void setHasRamp(boolean hasRamp) {
		this.hasRamp = hasRamp;
	}

	public String getRampMins() {
		return rampMins;
	}

	public void setRampMins(String rampMins) {
		this.rampMins = rampMins;
	}

	public String getRamp() {
		return ramp;
	}

	public void setRamp(String ramp) {
		this.ramp = ramp;
	}

	public boolean getHasEprCFlex() {
		return hasEprCFlex;
	}

	public void setHasEprCFlex(boolean hasEprCFlex) {
		this.hasEprCFlex = hasEprCFlex;
	}

	public String getEprCFlexDesc() {
		return eprCFlexDesc;
	}

	public void setEprCFlexDesc(String eprCFlexDesc) {
		this.eprCFlexDesc = eprCFlexDesc;
	}

	public Key<Product> getCurrentCpapMachine() {
		return currentCpapMachine;
	}

	public void setCurrentCpapMachine(Key<Product> currentCpapMachine) {
		this.currentCpapMachine = currentCpapMachine;
	}

	public String getCurrentCpapMachineSerial() {
		return currentCpapMachineSerial;
	}
	
	public void setCurrentCpapMachineSerial(String currentCpapMachineSerial) {
		this.currentCpapMachineSerial = currentCpapMachineSerial;
	}

	public String getCurrentCpapMachineHumidifierSerial() {
		return currentCpapMachineHumidifierSerial;
	}
	
	public void setCurrentCpapMachineHumidifierSerial(String currentCpapMachineHumidifierSerial) {
		this.currentCpapMachineHumidifierSerial = currentCpapMachineHumidifierSerial;
	}
	
	public Key<Product> getReplacementCpapMachine() {
		return replacementCpapMachine;
	}

	public void setReplacementCpapMachine(Key<Product> replacementCpapMachine) {
		this.replacementCpapMachine = replacementCpapMachine;
	}

	public String getReplacementCpapMachineSerial() {
		return replacementCpapMachineSerial;
	}

	public void setReplacementCpapMachineSerial(String replacementCpapMachineSerial) {
		this.replacementCpapMachineSerial = replacementCpapMachineSerial;
	}

	public String getReplacementCpapMachineHumidifierSerial() {
		return replacementCpapMachineHumidifierSerial;
	}

	public void setReplacementCpapMachineHumidifierSerial(
			String replacementCpapMachineHumidifierSerial) {
		this.replacementCpapMachineHumidifierSerial = replacementCpapMachineHumidifierSerial;
	}

	public Date getCpapPurchaseDate() {
		return cpapPurchaseDate;
	}

	public void setCpapPurchaseDate(Date cpapPurchaseDate) {
		this.cpapPurchaseDate = cpapPurchaseDate;
	}

	public Key<Product> getCurrentMask() {
		return currentMask;
	}

	public void setCurrentMask(Key<Product> currentMask) {
		this.currentMask = currentMask;
	}

	public Date getMaskPurchaseDate() {
		return maskPurchaseDate;
	}

	public void setMaskPurchaseDate(Date maskPurchaseDate) {
		this.maskPurchaseDate = maskPurchaseDate;
	}

	public String getAllergies() {
		return allergies;
	}

	public void setAllergies(String allergies) {
		this.allergies = allergies;
	}

	public String getHypertension() {
		return hypertension;
	}

	public void setHypertension(String hypertension) {
		this.hypertension = hypertension;
	}

	public String getSinusProblems() {
		return sinusProblems;
	}

	public void setSinusProblems(String sinusProblems) {
		this.sinusProblems = sinusProblems;
	}

	public String getCardiacHx() {
		return cardiacHx;
	}

	public void setCardiacHx(String cardiacHx) {
		this.cardiacHx = cardiacHx;
	}

	public String getDiabetes() {
		return diabetes;
	}

	public void setDiabetes(String diabetes) {
		this.diabetes = diabetes;
	}

	public String getChronicFaitque() {
		return chronicFaitque;
	}

	public void setChronicFaitque(String chronicFaitque) {
		this.chronicFaitque = chronicFaitque;
	}

	public String getAsthma() {
		return asthma;
	}

	public void setAsthma(String asthma) {
		this.asthma = asthma;
	}

	public String getThyroidHx() {
		return thyroidHx;
	}

	public void setThyroidHx(String thyroidHx) {
		this.thyroidHx = thyroidHx;
	}

	public String getEpitaxis() {
		return epitaxis;
	}

	public void setEpitaxis(String epitaxis) {
		this.epitaxis = epitaxis;
	}

	public String getBruxism() {
		return bruxism;
	}

	public void setBruxism(String bruxism) {
		this.bruxism = bruxism;
	}

	public String getSpecialMedicalNote() {
		return specialMedicalNote;
	}

	public void setSpecialMedicalNote(String specialMedicalNote) {
		this.specialMedicalNote = specialMedicalNote;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Key<Customer> getCustomer() {
		return customer;
	}

	public void setCustomer(Key<Customer> customer) {
		this.customer = customer;
	}

	public boolean getIsSmoker() {
		return isSmoker;
	}

	public void setIsSmoker(boolean isSmoker) {
		this.isSmoker = isSmoker;
	}

	public String getDentures() {
		return dentures;
	}

	public void setDentures(String dentures) {
		this.dentures = dentures;
	}

	public boolean getIsSleepsWithDentures() {
		return isSleepsWithDentures;
	}

	public void setIsSleepsWithDentures(boolean isSleepsWithDentures) {
		this.isSleepsWithDentures = isSleepsWithDentures;
	}

	public boolean getHasPet() {
		return hasPet;
	}

	public void setHasPet(boolean hasPet) {
		this.hasPet = hasPet;
	}

	public boolean getHasSleepPartner() {
		return hasSleepPartner;
	}

	public void setHasSleepPartner(boolean hasSleepPartner) {
		this.hasSleepPartner = hasSleepPartner;
	}

	public String getTravelFreq() {
		return travelFreq;
	}

	public void setTravelFreq(String travelFreq) {
		this.travelFreq = travelFreq;
	}

	public boolean getHasFacialHair() {
		return hasFacialHair;
	}

	public void setHasFacialHair(boolean hasFacialHair) {
		this.hasFacialHair = hasFacialHair;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getSleepPosition() {
		return sleepPosition;
	}

	public void setSleepPosition(String sleepPosition) {
		this.sleepPosition = sleepPosition;
	}

	public String getCpapPurchaseNotes() {
		return cpapPurchaseNotes;
	}

	public void setCpapPurchaseNotes(String cpapPurchaseNotes) {
		this.cpapPurchaseNotes = cpapPurchaseNotes;
	}

	public String getGerd() {
		return gerd;
	}

	public void setGerd(String gerd) {
		this.gerd = gerd;
	}

	public boolean getAllergiesFlag() {
		return allergiesFlag;
	}

	public void setAllergiesFlag(boolean allergiesFlag) {
		this.allergiesFlag = allergiesFlag;
	}

	public boolean getHypertensionFlag() {
		return hypertensionFlag;
	}

	public void setHypertensionFlag(boolean hypertensionFlag) {
		this.hypertensionFlag = hypertensionFlag;
	}

	public boolean getSinusProblemsFlag() {
		return sinusProblemsFlag;
	}

	public void setSinusProblemsFlag(boolean sinusProblemsFlag) {
		this.sinusProblemsFlag = sinusProblemsFlag;
	}

	public boolean getCardiacHxFlag() {
		return cardiacHxFlag;
	}

	public void setCardiacHxFlag(boolean cardiacHxFlag) {
		this.cardiacHxFlag = cardiacHxFlag;
	}

	public boolean getDiabetesFlag() {
		return diabetesFlag;
	}

	public void setDiabetesFlag(boolean diabetesFlag) {
		this.diabetesFlag = diabetesFlag;
	}

	public boolean getChronicFaitqueFlag() {
		return chronicFaitqueFlag;
	}

	public void setChronicFaitqueFlag(boolean chronicFaitqueFlag) {
		this.chronicFaitqueFlag = chronicFaitqueFlag;
	}

	public boolean getAsthmaFlag() {
		return asthmaFlag;
	}

	public void setAsthmaFlag(boolean asthmaFlag) {
		this.asthmaFlag = asthmaFlag;
	}

	public boolean getThyroidHxFlag() {
		return thyroidHxFlag;
	}

	public void setThyroidHxFlag(boolean thyroidHxFlag) {
		this.thyroidHxFlag = thyroidHxFlag;
	}

	public boolean getEpitaxisFlag() {
		return epitaxisFlag;
	}

	public void setEpitaxisFlag(boolean epitaxisFlag) {
		this.epitaxisFlag = epitaxisFlag;
	}

	public boolean getGerdFlag() {
		return gerdFlag;
	}

	public void setGerdFlag(boolean gerdFlag) {
		this.gerdFlag = gerdFlag;
	}

	public boolean getBruxismFlag() {
		return bruxismFlag;
	}

	public void setBruxismFlag(boolean bruxismFlag) {
		this.bruxismFlag = bruxismFlag;
	}

	public boolean getClaustrophobiaFlag() {
		return claustrophobiaFlag;
	}

	public void setClaustrophobiaFlag(boolean claustrophobiaFlag) {
		this.claustrophobiaFlag = claustrophobiaFlag;
	}

	public String getClaustrophobia() {
		return claustrophobia;
	}

	public void setClaustrophobia(String claustrophobia) {
		this.claustrophobia = claustrophobia;
	}

	public boolean getAcidRefluxSyndromeFlag() {
		return acidRefluxSyndromeFlag;
	}

	public void setAcidRefluxSyndromeFlag(boolean acidRefluxSyndromeFlag) {
		this.acidRefluxSyndromeFlag = acidRefluxSyndromeFlag;
	}

	public String getAcidRefluxSyndrome() {
		return acidRefluxSyndrome;
	}

	public void setAcidRefluxSyndrome(String acidRefluxSyndrome) {
		this.acidRefluxSyndrome = acidRefluxSyndrome;
	}

	public boolean getDroolingFlag() {
		return droolingFlag;
	}

	public void setDroolingFlag(boolean droolingFlag) {
		this.droolingFlag = droolingFlag;
	}

	public void setReferredDate(Date referredDate) {
		this.referredDate = referredDate;
	}
	
	public Date getReferredDate() {
		return referredDate;
	}

	public boolean getNightmaresFlag() {
		return nightmaresFlag;
	}

	public void setNightmaresFlag(boolean nightmaresFlag) {
		this.nightmaresFlag = nightmaresFlag;
	}

	public boolean getCopdFlag() {
		return copdFlag;
	}

	public void setCopdFlag(boolean copdFlag) {
		this.copdFlag = copdFlag;
	}

	public String getCopd() {
		return copd;
	}

	public void setCopd(String copd) {
		this.copd = copd;
	}

	public String getMedicationList() {
		return medicationList;
	}

	public void setMedicationList(String medicationList) {
		this.medicationList = medicationList;
	}

	public boolean getMorningHeadaches() {
		return morningHeadaches;
	}

	public void setMorningHeadaches(boolean morningHeadaches) {
		this.morningHeadaches = morningHeadaches;
	}

	public boolean getSleepingPills() {
		return sleepingPills;
	}
	
	public void setSleepingPills(boolean sleepingPills) {
		this.sleepingPills = sleepingPills;
	}
	
	public void setReferredBy(String referredBy) {
		this.referredBy = referredBy;
	}
	
	public String getReferredBy() {
		return referredBy;
	}

	
	
	public boolean getHighBloodPressureFlag() {
		return highBloodPressureFlag;
	}

	public void setHighBloodPressureFlag(boolean highBloodPressureFlag) {
		this.highBloodPressureFlag = highBloodPressureFlag;
	}

	public boolean getAtrialFibrillationFlag() {
		return atrialFibrillationFlag;
	}

	public void setAtrialFibrillationFlag(boolean atrialFibrillationFlag) {
		this.atrialFibrillationFlag = atrialFibrillationFlag;
	}

	public boolean getCongestedHeartFailureFlag() {
		return congestedHeartFailureFlag;
	}

	public void setCongestedHeartFailureFlag(boolean congestedHeartFailureFlag) {
		this.congestedHeartFailureFlag = congestedHeartFailureFlag;
	}

	
	
	public boolean getHalitosisFlag() {
		return halitosisFlag;
	}

	public void setHalitosisFlag(boolean halitosisFlag) {
		this.halitosisFlag = halitosisFlag;
	}

	@Override
	public String toString() {
		return "CustomerMedicalInfo [id=" + id + ", customer=" + customer
				+ ", familyDoctor=" + familyDoctor + ", sleepDoctor="
				+ sleepDoctor + ", clinic=" + clinic + ", referredBy="
				+ referredBy + ", referredDate=" + referredDate + ", isSmoker="
				+ isSmoker + ", dentures=" + dentures
				+ ", isSleepsWithDentures=" + isSleepsWithDentures
				+ ", hasPet=" + hasPet + ", hasSleepPartner=" + hasSleepPartner
				+ ", travelFreq=" + travelFreq + ", hasFacialHair="
				+ hasFacialHair + ", occupation=" + occupation
				+ ", sleepPosition=" + sleepPosition + ", medicationList="
				+ medicationList + ", diagnosis=" + diagnosis
				+ ", dateOfDiagnosticStudy=" + dateOfDiagnosticStudy
				+ ", dateOfCpapTitration=" + dateOfCpapTitration + ", ahi="
				+ ahi + ", o2Percent=" + o2Percent + ", longestApnea="
				+ longestApnea + ", existingCpapPatient=" + existingCpapPatient
				+ ", since=" + since + ", wakeUpUrinationEvents="
				+ wakeUpUrinationEvents + ", approxTimeOfSleepOnset="
				+ approxTimeOfSleepOnset + ", bedTimeHour=" + bedTimeHour
				+ ", hasSnore=" + hasSnore + ", hasTossAndTurn="
				+ hasTossAndTurn + ", hasDryMouth=" + hasDryMouth
				+ ", isShiftWorker=" + isShiftWorker + ", cpapPressure="
				+ cpapPressure + ", hasRamp=" + hasRamp + ", rampMins="
				+ rampMins + ", ramp=" + ramp + ", hasEprCFlex=" + hasEprCFlex
				+ ", eprCFlexDesc=" + eprCFlexDesc + ", currentCpapMachine="
				+ currentCpapMachine + ", currentCpapMachineSerial="
				+ currentCpapMachineSerial
				+ ", currentCpapMachineHumidifierSerial="
				+ currentCpapMachineHumidifierSerial
				+ ", replacementCpapMachine=" + replacementCpapMachine
				+ ", replacementCpapMachineSerial="
				+ replacementCpapMachineSerial
				+ ", replacementCpapMachineHumidifierSerial="
				+ replacementCpapMachineHumidifierSerial
				+ ", cpapPurchaseDate=" + cpapPurchaseDate + ", currentMask="
				+ currentMask + ", maskPurchaseDate=" + maskPurchaseDate
				+ ", cpapPurchaseNotes=" + cpapPurchaseNotes
				+ ", allergiesFlag=" + allergiesFlag + ", allergies="
				+ allergies + ", hypertensionFlag=" + hypertensionFlag
				+ ", hypertension=" + hypertension + ", sinusProblemsFlag="
				+ sinusProblemsFlag + ", sinusProblems=" + sinusProblems
				+ ", cardiacHxFlag=" + cardiacHxFlag + ", cardiacHx="
				+ cardiacHx + ", diabetesFlag=" + diabetesFlag + ", diabetes="
				+ diabetes + ", chronicFaitqueFlag=" + chronicFaitqueFlag
				+ ", chronicFaitque=" + chronicFaitque + ", asthmaFlag="
				+ asthmaFlag + ", asthma=" + asthma + ", thyroidHxFlag="
				+ thyroidHxFlag + ", thyroidHx=" + thyroidHx
				+ ", epitaxisFlag=" + epitaxisFlag + ", epitaxis=" + epitaxis
				+ ", gerdFlag=" + gerdFlag + ", gerd=" + gerd
				+ ", bruxismFlag=" + bruxismFlag + ", bruxism=" + bruxism
				+ ", claustrophobiaFlag=" + claustrophobiaFlag
				+ ", claustrophobia=" + claustrophobia
				+ ", acidRefluxSyndromeFlag=" + acidRefluxSyndromeFlag
				+ ", acidRefluxSyndrome=" + acidRefluxSyndrome
				+ ", droolingFlag=" + droolingFlag + ", nightmaresFlag="
				+ nightmaresFlag + ", copdFlag=" + copdFlag + ", copd=" + copd
				+ ", morningHeadaches=" + morningHeadaches + ", sleepingPills="
				+ sleepingPills + ", specialMedicalNote=" + specialMedicalNote
				+ "]";
	}	
}
