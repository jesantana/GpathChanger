package sanitas.markhand.estrategiabusqueda

import java.util.jar.Attributes;
import org.w3c.dom.NamedNodeMap

/**Estrategia de localizacion localizadora de nomenclatura incorrecta
 * 
 * Recibe un nodo XML que sera localizado en cualquiera de los dos casos siguientes
 * 1)El nodo es un boton coincidente y tiene un id distinto de ID_PARAMETRO
 * 
 * 2)El nodo es de tipo commandLink, posee un id distinto de ID_PARAMETRO y su padre es un boton coincidente
 * 
 * Un boton es coincidente si es un nodo de tipo commandButton, posee una imagen igual a TEXTO_IMAGEN_PARAMETRO 
 * 
 * 
 * @author KIKI
 * Marzo del 2012
 */
class PatCBNomenclatura extends PatCritBusqAbs  {
	
	def String imagen
	def String accion
	
	def PatCBNomenclatura()
	{
	
	}
	
	def static final String CNT_COMMAND_BUTTON="commandButton"
	def static final String CNT_COMMAND_LINK="commandLink"
	
	def closureBoton(strNombreElem,lstAtributos)
	{
		return (botonCoincidente(strNombreElem, lstAtributos)
			&& lstAtributos["id"]!=null
			&& !lstAtributos["id"].toLowerCase().contains(accion.toLowerCase()))
	}
	
	def closureLink(strNombreElem,lstAtributos,nodo)
	{
		return (strNombreElem.contains(CNT_COMMAND_LINK) 
			&& lstAtributos["id"]!=null
			&& !lstAtributos["id"].toLowerCase().contains(accion.toLowerCase()) 
			&& nodo.getParentNode()!=null 
			&& botonCoincidente(nodo.getParentNode().getNodeName(),procesaAtributos(nodo.getParentNode())))
	}
	
	public boolean encuentra(nodo) {
	
		def strNombreElem=nodo.getNodeName()
		def lstAtributos=procesaAtributos(nodo)
		
		//println "strNombreElem ${strNombreElem} lstAtributos ${lstAtributos}"
		//println "imagen ${imagen} accion ${accion}"
		
		return closureBoton(strNombreElem, lstAtributos) || closureLink(strNombreElem, lstAtributos, nodo)
	}
	
	public void organiza(Object lst) 
	{
		println "llamando a organiza con $lst"
		imagen=lst[0];
		accion=lst[1];
	}
	
	
	
	def boolean botonCoincidente(strNombreElem,lstAtributos)
	{
		return (strNombreElem.contains(CNT_COMMAND_BUTTON)
			&& lstAtributos["image"]!=null
			&& lstAtributos["image"].toLowerCase().equals(imagen.toLowerCase()))
	}

}
