package sanitas.markhand.estrategiasustitucion

abstract class PatSustitucion {

		
	
		abstract void organiza(lstDatosIniciales);
		abstract StringBuffer sustituye(StringBuffer strTexto,lstSustituciones);
}
