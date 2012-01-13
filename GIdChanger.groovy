package sanitas

import org.codehaus.groovy.ast.expr.ClosureListExpression;

class GIdChanger {
	
	
	def lstFiltroBusqueda
	
	def String componerNombre(accion,tipoComponente,fijo,orden)
	{
		"sg"+accion+tipoComponente+fijo+(System.currentTimeMillis()%1000)+orden
	}
	
	def Map encuentraSeleccionados(pagina,accion,fijo,nombreImagen,lstCumplen)
	{
		def node = new XmlSlurper().parseText(pagina)
		def result=[:]
		def out=node.depthFirst().findAll()
		{
			it->closureElemento(it, nombreImagen, accion)	
		}
		
		
		out.each
		{
			cajaTexto->
			def nombre=cajaTexto["@id"]
			def tipoComponente=""
			
			if(closureBoton(cajaTexto,nombreImagen,accion))
			{
				tipoComponente="Bt"	
			}
			else if(closureLink(cajaTexto,nombreImagen,accion))
			{
				tipoComponente="Lk"	
			}
			else
			{
				tipoComponente="Otr"	
			}
			
			lstCumplen[0]++
			def encontrado=lstCumplen[0]
			def nombreNuevo=componerNombre(accion,tipoComponente,fijo,encontrado)
			println "---------------Encontrado $nombre"
			println "---------------Cambiando id por $nombreNuevo . Es el $encontrado encontrado"
			
			result.put(nombre.toString(),nombreNuevo)
		}
		
		return result
	}
	
	
	
	def procesaArchivosJSPX(carpetaRaiz,prefijo,titulo,imagen)
	{
		def p = ~/.*\.jspx/
		def count=0
		def lstCumplen=[0]
		def mapaACambiar=[:]
		cargarFiltro()
		new File(carpetaRaiz).eachFileRecurse() {
			f ->
				
				if(f=~p)
				{
					println "Procesando $f"
					 if(!noPasaFiltro(f)){
							++count
							println "Archivo $count"
							mapaACambiar=encuentraSeleccionados(f.getText(),prefijo,titulo,imagen,lstCumplen)
							
							if(!mapaACambiar.isEmpty()){
								def cad=sustituyeSel(f.getText(), mapaACambiar)
								printFile(f.parentFile.toString()+"\\"+f.name.toString().replace(".","Test."),cad)
							}
					 }
					 else
					 {
					 	println "Eliminado por el filtro"
					 }
				}
		}
		
		
		
		println "Procesados finalmente $count archivos"
	}
	
	
	def procesaArchivosJSPX(carpetaRaiz,mapCom)
	{
		def p = ~/.*\.jspx/
		def count=0
		def lstCumplen=[0]
		def mapaACambiar=[:]
		cargarFiltro()
		new File(carpetaRaiz).eachFileRecurse() {
			f ->
				mapaACambiar.clear()
				if(f=~p)
				{
					//println "Procesando $f"
					 if(!noPasaFiltro(f)){
							++count
							//println "Archivo $count"
							mapCom.inject(mapaACambiar) 
							{  
								mapa,it->
								println "Haciendo  ${it['prefijo']}   ${it['accion']} ${it['imagen']} en $f"
								mapa<<encuentraSeleccionados(f.getText(),it["prefijo"],it["accion"],it["imagen"],lstCumplen)	
							}
							
							
							if(!mapaACambiar.isEmpty()){
								println "mapaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaAcambiarrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr   $mapaACambiar"
								
								def cad=sustituyeSel(f.getText(), mapaACambiar)
								printFile(f.parentFile.toString()+"\\"+f.name.toString().replace(".","Test."),cad)
							}
					 }
					 else
					 {
						 println "Eliminado por el filtro"
					 }
				}
		}
		
		
		
		println "Procesados finalmente $count archivos"
	}
	
	def cargarFiltro()
	{
		if(lstFiltroBusqueda==null)
		{
			lstFiltroBusqueda=[]	
		}
		
		lstFiltroBusqueda<<"svn"
		lstFiltroBusqueda<<"sfc"
		lstFiltroBusqueda<<"generales"
		lstFiltroBusqueda<<"sfc"
		lstFiltroBusqueda<<"widgets"
	}
	
	def boolean noPasaFiltro(f)
	{
		lstFiltroBusqueda.any
		{
			filtro->
			
			f=~filtro
				
		}
	}
	
	def closureBoton(obj,imagen,accion)
	{
		return (obj.name()=="commandButton" && obj["@image"].text().equals(imagen) && !obj["@id"].text().contains(accion))
	}
	
	def closureLink(obj,imagen,accion)
	{
		return (obj.name()=="commandLink" && !obj["@id"].text().contains(accion) && 
			obj.parent()!=null && closureBoton(obj.parent(),imagen,accion))
	}
	
	def closureElemento(obj,imagen,accion)
	{
		return closureBoton(obj, imagen, accion) || closureLink(obj, imagen, accion)
	}
	
	def printFile(filePath,xml)
	{
		
		def writer = new File(filePath)
		writer.write(xml)
	}
	
	def String sustituyeSel(pagina,mapChanges)
	{
		def auxPagina=pagina	
		
		auxPagina=mapChanges.inject(auxPagina) {texto,k
				->
				
				texto=texto.replaceAll(k.getKey(),k.getValue())
			}
		
		return auxPagina
	}
 
	static void main(args)
	{
		GIdChanger gidChanger=new GIdChanger()
		
		println args.length
		switch(args.length)
		{
			case 4:
				def carpeta=args[0]
				def prefijo=args[1]
				def accion=args[2]
				def imagen=args[3]
				gidChanger.procesaArchivosJSPX(carpeta,prefijo,accion,imagen)
				break
			case 1:
				def fileName=args[0]
				File f=new File(fileName)
				println f
				def mapComandos=[]
				def carpeta=""
				f.eachLine 
				{  
					linea->
					def lstArg=linea.tokenize("\t")
					carpeta=lstArg[0]
					def prefijo=lstArg[1]
					def accion=lstArg[2]
					def imagen=lstArg[3]
					
					def record=["prefijo":prefijo,"accion":accion,"imagen":imagen]
					
					mapComandos<<record
					
					println "carpeta $carpeta"
					println "prefijo $prefijo"
					println "accion $accion"
					println "imagen $imagen"
					
					
					
					
				}
				gidChanger.procesaArchivosJSPX(carpeta,mapComandos)
				break
			default:
				println """El script tiene 2 posibles entradas: 
						4 Argumentos:Carpeta Raiz,Prefijo a localizar,Accion del boton, Imagen del boton
						1 Argumento:Ruta del archivo con la entrada a tratar donde cada linea del mismo es segun la opcion anterior"""
		}
		
	}  
}
