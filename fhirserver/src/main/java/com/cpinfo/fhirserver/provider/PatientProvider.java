package com.cpinfo.fhirserver.provider;

import java.util.List;

import com.cpinfo.fhirserver.service.PatientService;
import com.cpinfo.fhirserver.util.AppCtx;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.exceptions.InvalidResponseException;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;

public class PatientProvider implements IResourceProvider {

	private PatientService service = (PatientService) AppCtx.ctx
			.getBean("patientService");

	public Class<Patient> getResourceType() {
		return Patient.class;
	}

	@Create()
	public MethodOutcome createPatient(@ResourceParam Patient thePatient) {
		try {
			Patient p = service.createPatient(thePatient);// 调用业务层来创建一个patient
			return new MethodOutcome(p.getId(), true);
		} catch (Exception e) {
			e.printStackTrace();
			OperationOutcome opcome = new OperationOutcome();
			opcome.addIssue()
			.setSeverity(IssueSeverityEnum.FATAL)
			.setDetails(new CodeableConceptDt()
			.setText("无效的资源"));
			throw new UnprocessableEntityException("", opcome);
		}
	}

	@Read(version = false)
	public Patient readPatient(@IdParam IdDt id) {
		try {
			System.out.println("readPatient.id=" + id);
			Patient p = service
					.readPatient(id.getValue().split("/")[1]);
			return p;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceNotFoundException(id);
		}
	}

	@Update()
	public MethodOutcome updatePatient(@IdParam IdDt id,
			@ResourceParam Patient patient) {
		System.out.println("updatePatient.id" + id);
		try {
			patient.setId(id.getValue().split("/")[1]);
			patient = service.updatePatient(patient);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidResponseException(400, e.getMessage());
		}
		return new MethodOutcome(patient.getIdElement());
	}

	@Search()
	public List<Patient> findPatientsByName(@RequiredParam(name = Patient.SP_NAME) StringDt name) {
		try {
			System.out.println("findPatientsByName.name=" + name);
			List<Patient> list = service
					.findPatientByName(name.getValueAsString());
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidResponseException(400, e.getMessage());
		}
	}

	@Delete
	public void deletePatient(@IdParam IdDt id) {
		System.out.println("deletePatient.id=" + id);
		try {
			int num = service.delPatient(id.getValue().split("/")[1]);
			if (num <= 0)
				throw new ResourceNotFoundException("未找到");
		} catch (Exception e) {
			throw new InvalidResponseException(400, e.getMessage());
		}
	}

	@History()
	public List<Patient> getPatientHistory(@IdParam IdDt id) {
		try {
			System.out.println("getPatientHistory.id=" + id);
			List<Patient> list = service
					.findHisPatient(id.getValue().split("/")[1]);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidResponseException(400, e.getMessage());
		}
	}
}
