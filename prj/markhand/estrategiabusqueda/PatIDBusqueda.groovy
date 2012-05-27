package sanitas.markhand.estrategiabusqueda

import sanitas.markhand.util.Util

class PatIDBusqueda extends PatCritBusqAbs {

	static def ICE="ice:";
	
	@Override
	public boolean encuentra(obj) {
		def strNombreElem=obj.getNodeName()
		def lstAtributos=procesaAtributos(obj)
		
		//println "strNombreElem ${strNombreElem} lstAtributos ${lstAtributos} id ${lstAtributos['id']}"
		
		
		return strNombreElem.contains(ICE) && lstAtributos["id"]==null
	}

	@Override
	public void organiza(Object lst) {
		sustituidor=Util.cargaClaseDesdeRuta("${Util.rutaAplicacion}\\${lst[0]}")
		//println "cargado Sustituidor $sustituidor"
	}

}
