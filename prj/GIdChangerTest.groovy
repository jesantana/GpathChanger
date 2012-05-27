package sanitas

import groovy.util.GroovyTestCase;
import kataenerore.StringCalculatorRE
import util.GUtil

class GIdChangerTest extends GroovyTestCase {
	
	GIdChanger gidChanger = new GIdChanger()
	
	void test1()
	{
		gidChanger.procesaArchivosJSPX('D:\\Programas\\Sanitas\\DSS101414- BW Consola\\ViewController\\public_html\\jsp',"Al","Aceptar","../sfc/img/bwAceptar.png")	
	}
	/*
	void test2()
	{
		def xml = new XmlParser().parse('D:\\Programas\\Sanitas\\DSS101414- BW Consola\\ViewController\\public_html\\jsp\\schemas\\FormSchema.jspx')
		
		gidChanger.printFile('D:\\Programas\\Sanitas\\DSS101414- BW Consola\\ViewController\\public_html\\jsp\\schemas\\FormSchemaTEST.jspx', xml)
	}
	*/
	void test3()
	{
		def cadena="hola mundo que pasa aqui tengo que meterle a todo el mundo pa que me respete o que ponganse pa esto que formo un pollo"
		def mapa=['que':'e','mundo':'gente']
		
		println gidChanger.sustituyeSel(cadena, mapa)
	}
	
	void test4()
	{
		File f=new File("D:\\Programas\\Sanitas\\DSS101414- BW Consola\\ViewController\\public_html\\jsp\\schemas\\FormSchema.jspx")
		print f.parentFile.toString()+"\\"+f.name.replace(".","Test.")
	}

}
