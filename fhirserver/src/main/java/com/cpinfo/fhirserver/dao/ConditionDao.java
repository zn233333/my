package com.cpinfo.fhirserver.dao;

import java.util.List;
import java.util.Map;

public interface ConditionDao {
	
	void creat(Map<String, String> map);

	Map<String, String> findById(String id);

	List<Map<String, String>> findByPatientId(String patientId);
	
	List<Map<String, String>> findByAsserterId(String asserterId);

	List<Map<String, String>> findByName(String name);
	
	List<Map<String, String>> findHisById(String id);

	int delById(String id);

	void setActive(String id);

	void update(Map<String, String> conditionMap);
}
