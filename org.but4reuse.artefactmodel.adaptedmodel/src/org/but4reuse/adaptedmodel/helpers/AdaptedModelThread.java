package org.but4reuse.adaptedmodel.helpers;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.AdaptedModelFactory;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.helper.AdaptersHelper;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * 
 * Thread used to adapt one artefact into an adapted artefact.
 * 
 * @author Julien Leroux
 *
 */
public class AdaptedModelThread extends Thread{

	AdaptedArtefact adaptedArtefact;
	AdaptedModel adaptedModel;
	Artefact artefact;
	List<IAdapter> adapters;
	IProgressMonitor monitor;
	AdaptedArtefact[] tab;
	int num;
	
	
	/**
	 * 
	 * Constructor for the thread
	 * 
	 * 
	 * @param tabAdp
	 * @param adaptedMod
	 * @param art
	 * @param adapts
	 * @param mon
	 * @param index
	 */
	AdaptedModelThread(AdaptedArtefact[] tabAdp, AdaptedModel adaptedMod, Artefact art,List<IAdapter> adapts, IProgressMonitor mon,int index){
		adaptedArtefact = tabAdp[index];
		tab = tabAdp;
		adaptedModel=adaptedMod;
		artefact = art;
		adapters = adapts;
		monitor = mon;
		num = index;
	}
	/**
	 * 
	 * Running operation of the thread wich adapts the artefact and places it on the right index in the array of adapted artefacts
	 * 
	 * 
	 */
	public void run() {
	    
		adaptedArtefact.setArtefact(artefact);
		
		
		String name = artefact.getName();
		if (name == null || name.length() == 0) {
			name = artefact.getArtefactURI();
		}
		monitor.subTask("Adapting: " + name);

		List<IElement> list = AdaptersHelper.getElements(artefact, adapters);
		for(IElement ele : list){
			ElementWrapper ew = AdaptedModelFactory.eINSTANCE.createElementWrapper();
			ew.setElement(ele);
			adaptedArtefact.getOwnedElementWrappers().add(ew);
		}
		tab[num]=adaptedArtefact;
		
		monitor.worked(1);
		if (monitor.isCanceled()) {
			this.stop();
		}
		
	  }
	
}