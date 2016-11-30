package com.esofa.gae.datamigration;

import static com.esofa.crm.service.OfyService.ofy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.cmd.Query;

/**
 * Utility to migrate information within a single entity
 */
public class DataMigrationProcessor<E> {
	public static enum Action {CHANGE, DELETE};
	
	private static final Logger log = Logger.getLogger(DataMigrationProcessor.class.getName());
	
	private static final int BATCH_LIMIT = 100;
	
	private final Class<E> entityClass;
	private final Method callbackMethod;
	private final List<Entry> filters;
	private final Action action;
	
	private class Entry {
		private final String filterCondition;
		private final Object filterValue;
		
		Entry(String filterCondition, Object filterValue) {
			this.filterCondition = filterCondition;
			this.filterValue = filterValue;
		}
	}
	
	/**
	 * 
	 * @param entityClass - class represented by E... hack to get around Java generic constraint
	 * @param callbackMethod - 
	 */
	public DataMigrationProcessor(Class<E> entityClass, Method callbackMethod, Action action ) {
		if( !entityClass.isAnnotationPresent(Entity.class) ) {
			throw new IllegalArgumentException("Target class " + entityClass + " is not an Objectify Entity.");
		}		
		this.entityClass = entityClass;
		this.callbackMethod = callbackMethod;
		this.filters = new ArrayList<Entry>(10);
		this.action = action;
	}
	
	public void addFilter(String condition, Object value) {
		// filter needs to be on a field that is indexed
		// if want to can add the check to enforce it
		this.filters.add(new Entry(condition, value));
	}

	public String mock() {
		return mock(null);
	}
	public String mock(String webSafeCursorString) {
		return executeProcessor(webSafeCursorString, false);
	}
	
	public String run() {
		return run(null);
	}
	public String run(String webSafeCursorString) {
		return executeProcessor(webSafeCursorString, true);
	}
	
	private String executeProcessor(String webSafeCursorString, boolean persistChanges) {
		QueryResultIterator<E> victims = getAllVictims(webSafeCursorString);
		if( !victims.hasNext() ) return null;
		Cursor nextStart = change(victims, persistChanges); 
		return nextStart == null ? null : nextStart.toWebSafeString();				
	}
	
	private QueryResultIterator<E> getAllVictims(String webSafeCursorString) {
		Query<E> query = ofy().load().type(entityClass).limit(BATCH_LIMIT);
		for( Entry e : filters ) {
			query = query.filter(e.filterCondition, e.filterValue);
		}
		if( webSafeCursorString != null ) {
			query = query.startAt(Cursor.fromWebSafeString(webSafeCursorString));
		}
		return query.iterator();
	}
	
	// change all entities based on the predefined rule	
	private Cursor change(QueryResultIterator<E> victims, boolean persistChanges) {
		try {
			// callback to deal with the change?
			List<E> changedVictims = new ArrayList<E>(BATCH_LIMIT);
			while( victims.hasNext() ) {
				E victim = victims.next();				
				String preChange = victim.toString();
				Object result = callbackMethod.invoke(victim);
				Boolean changed = Boolean.FALSE;
				
				if (result instanceof Boolean) {
					
					changed = (Boolean) result;
				} else if (result instanceof String) {
					
					String resultStr = (String) result;
					
					if (StringUtils.isNotEmpty(resultStr)) {
						changed = Boolean.TRUE;
					}
				}
				//Boolean changed = (Boolean) callbackMethod.invoke(victim);
				
				if( changed ) {
					
					String postChange = victim.toString();
					changedVictims.add(victim);
					log.info("Prechange : " + preChange);
					log.info("Postchange: " + postChange);
				}
			}
			if( persistChanges ) {
				if( this.action == DataMigrationProcessor.Action.CHANGE) {
					persist(changedVictims);
				} else if( this.action == DataMigrationProcessor.Action.DELETE) {
					delete(changedVictims);
				}
			}
			return victims.getCursor();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// persist the changes permanently
	private void persist(List<E> victims) {
		log.severe("Saving victims: " + victims.size());
		ofy().save().entities(victims);
	}
	
	private void delete(List<E> victims) {
		log.info("Deleting " + victims.size() + " victims.");
		ofy().delete().entities(victims).now();
	}
}
