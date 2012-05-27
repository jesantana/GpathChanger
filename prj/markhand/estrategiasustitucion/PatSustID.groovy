package sanitas.markhand.estrategiasustitucion

import sanitas.markhand.util.Util

class PatSustID extends PatSustitucion {
	
	def static orden=0
	
	public void organiza(lstDatosIniciales)
	{
		
	}
	
	StringBuffer sustituye(StringBuffer strTexto,lstSustituciones)
	{
		def cadena=strTexto

			def posComienzo=Util.posFromLineCol(cadena,lstSustituciones.lineaComienzo,lstSustituciones.columnaComienzo)
		
			

			def posEspacio=compruebaSiExiste(cadena.indexOf(' ',posComienzo))
			def posAngular=compruebaSiExiste(cadena.indexOf('/>',posComienzo))
			def posAngularSolo=compruebaSiExiste(cadena.indexOf('>',posComienzo))


			def pos=Math.min(Math.min(posEspacio,posAngular),posAngularSolo)
			//println posComienzo+"  "+cadena[posComienzo]
			//println "SUSTITUYENDO ${cadena.substring(posComienzo-5,posComienzo)} posComienzo=${posComienzo} id=${(System.currentTimeMillis()%1000)+(orden)}"
			cadena=cadena.insert(pos, " id=\"cmp"+(System.currentTimeMillis())+"oo"+(orden++)+"\" ")
			
		return cadena;
	}
	
	def compruebaSiExiste(num)
	{
		return num<0?Integer.MAX_VALUE:num	
	}
}
