package sanitas.markhand

import org.apache.ivy.core.settings.IvySettings.ModuleSettings;
import org.xml.sax.InputSource

import sanitas.markhand.estrategiabusqueda.PatCritBusqAbs;
import sanitas.markhand.estrategialocalizacion.PatLocalizacionAbs;
import sanitas.markhand.estrategiasustitucion.PatSustitucion
import sanitas.markhand.util.LecturaHandler
import sanitas.markhand.util.PositionalXMLReader
import sanitas.markhand.util.Util
import groovy.util.CharsetToolkit
import java.nio.charset.Charset

class Pruebas {
	
	List<PatCritBusqAbs> criteriosLocalizar
	List lstFiltroBusqueda
	
	
	
	def Pruebas()
	{
		criteriosLocalizar=[]
		Util.rutaAplicacion= new File(getClass().protectionDomain.codeSource.location.path).parent
		lstFiltroBusqueda=[]
	}
	
	static void main(args)
	{
		Pruebas p=new Pruebas()
		switch(args.length)
		{
			case 1:
				def path=args[0]
				def archivosProcesar=[]
				if(Util.esPaginaJSPX(path))
				{
					File archivoPath=new File(path)
					archivosProcesar<<archivoPath
					path=archivoPath.parentFile.toString()
					println archivoPath	
				}
		
				
				p.procesaArchivosJSPX(path,archivosProcesar)
				
				break
			default:
				println """El script tiene 2 posibles entradas:
						4 Argumentos:Carpeta Raiz,Prefijo a localizar,Accion del boton, Imagen del boton
						1 Argumento:Ruta del archivo con la entrada a tratar donde cada linea del mismo es segun la opcion anterior"""
		}
		//p.procesaArchivosJSPX("D:\\Programas\\Sanitas\\DSS101414- BW Consola\\ViewController\\public_html\\jsp\\procesos")
	}
	
	
	/**Procesa el texto XML del archivo actual aplicandole cada una de las estrategias de localizacion
	 * cargadas previamente.
	 * 
	 * Para cada coincidencia localizada, de momento la imprime
	 * 
	 * @param f
	 * @return
	 */
	def String procesaArchivo(File f)
	{
		def texto=f.getText();
		def StringBuffer sbf=null
		Charset ct=new CharsetToolkit(f).getCharset()
		println("**********************************CHARSET "+ct.name())		

		
		LecturaHandler lh=new LecturaHandler()
		def inputStream = new ByteArrayInputStream(texto.getBytes("UTF-8"))
		println "Procesando $f"
		PositionalXMLReader.readXML(inputStream,lh,criteriosLocalizar)
		println "-------------------------------------Coincidencias para archivo $f"
		
		if(!lh.listaLocalizaciones.isEmpty()){
		
			sbf=new StringBuffer(texto)
			
			lh.listaLocalizaciones.each 
			{  
				println it.nodo.getNodeName()+" comienzo"+it.lineaComienzo+","+it.columnaComienzo+" fin"+it.lineaFin+","+it.columnaFin
				
				if(it.criterioBusqueda.sustituidor!=null){
					it.criterioBusqueda.sustituidor.sustituye(sbf,it)
				}
				
				//println sbf.toString()
			}
			
			Util.printFile(f.parentFile.toString()+"\\"+f.name.toString().replace(".","Test."),sbf.toString(),ct.name())
		}
		
	}
	
	/**
	 * El metodo dada una carpeta raiz carga todos los parametros del proceso
	 * 
	 * Por cada uno de los archivos(recursivos) que encuentre en la CR
	 * 
	 * ------Mira que acaben en jspx
	 * ------Mira que no contengan palabras del filtro
	 * ------Lo envia a procesar
	 * 
	 * @param carpetaRaiz Ruta de la carpeta inicial
	 * @return
	 */
	def procesaArchivosJSPX(carpetaRaiz,lstArchivosProcesar)
	{
		cargarFiltro()
		//println lstFiltroBusqueda
		
		cargarLocalizadores()
		//println criteriosLocalizar
		
		cargarSustituidores()
		//println criteriosSustitucion
		new File(carpetaRaiz).eachFileRecurse() {
			f ->
				if(Util.esPaginaJSPX(f))
				{
					
					 if(!noPasaFiltro(f,lstArchivosProcesar)){
						 //println "Procesando $f"
						 	def textoTratado=procesaArchivo(f)
							//printFile(f.parentFile.toString()+"\\"+f.name.toString().replace(".","Test."),textoTratado)
					 }
					 else
					 {
						 //println "Eliminado por el filtro $f"
					 }
				}
		}
		
		
		
		println "FIN"
	}
	
	
	/*
	 * Las palabras por las que se desea filtrar deberan incluirse en la misma ruta de la aplicacion en el archivo
	 * filtro.properties. Una vez definidas las mismas al analizar los archivos del proyecto si los mismos en su ruta completa
	 * inlcuida carpeta, contienen una de las palabras definidas como filtro, seran descartados del analisis
	 */
	def cargarFiltro()
	{
		
		def fileNameShort="filtro.properties"
		def fileName="${Util.rutaAplicacion}\\$fileNameShort"
		
		File f=new File(fileName)
		
		f.eachLine
		{
			linea->
			
			lstFiltroBusqueda<<linea
		}
	}
	
	def boolean noPasaFiltro(f,lstArchivosProcesar)
	{
		boolean result=true
		
		if(lstArchivosProcesar.isEmpty() || (!lstArchivosProcesar.isEmpty() && lstArchivosProcesar.contains(f)))
		{
		
			result=lstFiltroBusqueda.any
			{
				filtro->
				f=~filtro
			}
		
		}
		
		return result
	}
	
	
	
	
	/*
	* Las estrategias de localizacion deseable deberan incluirse en la misma ruta de la aplicacion en el archivo
	* localizadores.properties. 
	* 
	* La manera en que se incluiran sera la siguiente
	* 
	* Cada linea comenzara con una clase heredera de PatCritBusqAbs seguida de tantos argumentos como criterios necesite el metodo
	* organizar de la misma. Cada uno de los elementos estara separado por tabulador
	* 
	* Las estrategias de localizacion sirven para definir los fragmentos del xml que se desean localizar
	*/
	def cargarLocalizadores()
	{
		
		def fileNameShort="localizadores.properties"
		leeYRellenaProperties(Util.rutaAplicacion, fileNameShort, criteriosLocalizar)
		//println "Situando ${criteriosLocalizar}.size() al Handler de Lectura"
	}
	
	def cargarSustituidores()
	{
		
		def fileNameShort="sustituidores.properties"
		//leeYRellenaProperties(path, fileNameShort, criteriosSustitucion)
	}
	
	def leeYRellenaProperties(path,fileNameShort,lstGuardar)
	{
		
		def fileName="$path\\$fileNameShort"
		File f=new File(fileName)
		f.eachLine
		{
			linea->
			if(!linea.trim().startsWith("--")){
				def lstArg=linea.tokenize("\t")
				def clase=lstArg[0]
				
				GroovyObject groovyObject = Util.cargaClaseDesdeRuta("$path\\$clase")
				lstArg.remove(0);
				
				groovyObject.organiza(lstArg);
				lstGuardar<<groovyObject
			}
		}
	}
}
