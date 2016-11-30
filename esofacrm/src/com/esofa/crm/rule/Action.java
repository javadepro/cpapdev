package com.esofa.crm.rule;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.esofa.crm.messenger.model.WorkPackage;

@Component
public interface Action<T extends Serializable, S extends Serializable> {
	
	void execute(WorkPackage<T, S> workPackage);

}
