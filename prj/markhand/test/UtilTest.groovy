package sanitas.markhand.test;

import groovy.util.GroovyTestCase
import sanitas.markhand.util.Util

class UtilTest extends GroovyTestCase {

	void test3()
	{
		def cadena="""hola mundo
el mundo pa
fdñslfñdslf
ksdfñkf
ls
		skdfkjlsdj
		jkldjsdk jkfdsjlfdsjlk dskfjdslkjlkefew fjslkjfklsdjfl
		pollo"""
		

		assert Util.posFromLineCol(cadena, 1, 1)==0
		assert Util.posFromLineCol(cadena, 2, 4)==14
		assert Util.posFromLineCol(cadena, 5, 2)==44
		assert Util.posFromLineCol(cadena, 12, 2)==-1
		assert Util.posFromLineCol(cadena, 1, 20)==-1
		
	}
	
	void test4()
	{
		def cadena="hola <mundo> l mund<o pafdsdk jkfdsjlfdspollo"
		

		assert Util.posAngularComienzo(cadena, 1)==-1
		assert Util.posAngularComienzo(cadena, 10)==5
		assert Util.posAngularComienzo(cadena, 30)==19
		
		cadena="        <ice:outputStyle href='../sfc/css/xp.css'></ice:outputStyle>"
		
		assert Util.posAngularComienzo(cadena, 47)==5
	}
}
