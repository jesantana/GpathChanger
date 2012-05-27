package sanitas.markhand.estrategiasustitucion

class PatSusNomenclatura extends PatSustitucion {
	
	def String accionAnterior
	def String accionFutura
	
	
	void organiza(lstDatosIniciales)
	{
		
	}
	
	
	StringBuffer sustituye(StringBuffer strTexto,lstSustituciones)
	{
		def nombreIdAntiguo=accionAnterior
		def pattern= ~ /id=(!?\S)*md(!?\S)*/
		def matcher=pattern.matcher(strTexto)
		def StringBuffer sbString=new StringBuffer()
		
		def textoNuevo=lstSustituciones.inject(strTexto)
		{
			texto,k->
			def matcherInterno=matcher.region(Util.posFromLineCol(k.lineaComienzo,k.columnaComienzo),Util.posFromLineCol(k.lineaFin,k.columnaFin))
			println "region start ${matcher.regionStart()} region end ${matcher.regionEnd()}"
			
			if(matcher.find())
			{
				matcher.appendReplacement(sbString,"id=bbbbb")
			}
			matcher.appendTail(sbString)
			texto=sbString.toString()
		}
		
		return textoNuevo;
	}
	

}
