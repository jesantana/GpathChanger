package sanitas.markhand.util

public class Util {

	static String rutaAplicacion
	private static def p = ~/.*\.jspx/
	
	def static posFromLineCol(strCad,lineaFinal,columnaFinal)
	{
		//println "Llamando a posFromLineCol con ${lineaFinal}---${columnaFinal}"
		
		def linea=1
		def columna=1
		def intPosCount=0
		def encontrado=false
		def result=-1
		
		if(lineaFinal==null || columnaFinal==null)
		{
			throw new Exception("Datos de entrada invalidos")
		}
		
		strCad.toString().each()
		{
			if(!encontrado)
			{
				if(linea==lineaFinal && columna==columnaFinal)
				{
					encontrado=true
				}
				else if(linea<lineaFinal || (linea==lineaFinal && columna<columnaFinal))
				{
					columna++
					intPosCount++
					if("\n"==it)
					{
						linea++
						columna=1
					}
				}
			}
			else
			{
				result=intPosCount;	
			}
		}
		
		//println linea+"            "+columna
		
		return result
	}
	
	
	
	def static cargaClaseDesdeRuta(ruta)
	{
		GroovyClassLoader loader = new GroovyClassLoader()
		Class groovyClass = loader.parseClass(new File(ruta))
		
		return groovyClass.newInstance();
	}
	
	def static printFile(filePath,text)
	{
		
		def writer = new File(filePath)
		writer.write(text,"UTF-8")
		
	}
	def static printFile(filePath,text,charset)
	{
		
		def writer = new File(filePath)
		writer.write(text,charset)
		
	}
	
	def static esPaginaJSPX(file)
	{
		return file=~p
	}
}
