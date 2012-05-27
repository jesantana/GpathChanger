package sanitas.markhand.estrategiabusqueda;

import java.util.Map;

import org.w3c.dom.NamedNodeMap
import sanitas.markhand.estrategiasustitucion.PatSustitucion;

abstract class PatCritBusqAbs {

	public PatSustitucion sustituidor
	
	abstract boolean encuentra(obj);
	abstract void organiza(lst);
	
	
	def Map procesaAtributos(nodo)
	{
		Map result=[:]
		NamedNodeMap attr=nodo.getAttributes()
		
		for(int i=0;i<attr.getLength();i++)
		{
			result[attr.item(i).getNodeName()]=attr.item(i).getNodeValue()
		}
		
		return result;
	}
}
