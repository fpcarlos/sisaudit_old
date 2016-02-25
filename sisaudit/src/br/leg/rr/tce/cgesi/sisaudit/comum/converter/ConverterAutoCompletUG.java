package br.leg.rr.tce.cgesi.sisaudit.comum.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.leg.rr.tce.cgesi.sisaudit.comum.entity.UnidadeGestora;

@FacesConverter("converterAutoCompletUG")
public class ConverterAutoCompletUG implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		UnidadeGestora unidG = new UnidadeGestora();
		return unidG;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		
		return " ";
	}

	

	
	
}
