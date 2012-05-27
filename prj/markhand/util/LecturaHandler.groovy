package sanitas.markhand.util


import sanitas.markhand.estrategialocalizacion.PatLocalizacionAbs



import org.xml.sax.Locator
import org.xml.sax.helpers.DefaultHandler;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.List

import org.w3c.dom.Element
import org.w3c.dom.Node
import org.xml.sax.Attributes
import org.xml.sax.Locator
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler

import sanitas.markhand.estrategiabusqueda.PatCritBusqAbs
import sanitas.markhand.estrategialocalizacion.PatLocalizacionAbs
import org.w3c.dom.Document

class LecturaHandler extends DefaultHandler { 
	
	private Locator locator;
	List<PatCritBusqAbs> filtrosLocalizacion
	List<PatLocalizacionAbs> listaLocalizaciones
	
	final Stack<Element> elementStack = new Stack<Element>()
	Document doc
	int ultimaLocalizacionX
	int ultimaLocalizacionY
	
	
			public void organizarHandler(doc,filtrosLocalizacion)
			{
				this.doc=doc
				this.filtrosLocalizacion=filtrosLocalizacion
				
			}		
	
			@Override
			public void startDocument() {
				this.listaLocalizaciones = []
				ultimaLocalizacionX=0
				ultimaLocalizacionY=0
			}
	
			@Override
            public void setDocumentLocator(final Locator locator) {
                this.locator = locator; // Save the locator, so that it can be used later for line tracking when traversing nodes.
            }

            @Override
            public void startElement(final String uri, final String localName, final String qName, final Attributes attributes)
                    throws SAXException {
                def List<PatLocalizacionAbs> tempElemEncont;
						
				
                
                final Element el = doc.createElement(qName);
                for (int i = 0; i < attributes.getLength(); i++) {
                    el.setAttribute(attributes.getQName(i), attributes.getValue(i));
                }
                
				
				el.setUserData(Constantes.ELEMENTO_LOCALIZADO,ultimaLocalizacionY, null);
				el.setUserData(Constantes.ELEMENTO_LOCALIZADO_COL,ultimaLocalizacionX, null);
				
				//println "Elemento $qName comienza en  $ultimaLocalizacionY $ultimaLocalizacionX"
				
                elementStack.push(el);
				situarUltimaLocalizacion(locator)
            }

            @Override
            public void endElement(final String uri, final String localName, final String qName) {
                final Element closedEl = elementStack.pop();
				def count=0
				
				situarUltimaLocalizacion(locator)
				if (elementStack.isEmpty()) { // Is this the root element?
                    doc.appendChild(closedEl);
                } else {
                    final Element parentEl = elementStack.peek();
                    parentEl.appendChild(closedEl);
                }
				
				filtrosLocalizacion.each
				{
					
					//println "APlicando filtro $it"
					if(it.encuentra(closedEl))
					{
						//println ">>>>>>>>>>>>>>>Filtro $it ha localizado el archivo anterior"
						PatLocalizacionAbs localizado=new PatLocalizacionAbs()
						localizado.nodo=closedEl
						
						localizado.lineaComienzo=closedEl.getUserData(Constantes.ELEMENTO_LOCALIZADO)
						localizado.columnaComienzo=closedEl.getUserData(Constantes.ELEMENTO_LOCALIZADO_COL)
						
						localizado.lineaFin=this.locator.getLineNumber()
						localizado.columnaFin=this.locator.getColumnNumber()
						
						localizado.criterioBusqueda=it
						
						listaLocalizaciones<<localizado
					}
				}
				
				//println "Elemento $qName acaba  en  ${this.locator.getLineNumber()} ${this.locator.getColumnNumber()}"
				
            }

            @Override
            public void characters(final char[] ch, final int start, final int length) throws SAXException {
				//println "caracteres ${String.valueOf(ch).substring(start,start+length)} acaba en  ${locator.getLineNumber()} ${locator.getColumnNumber()}"
				situarUltimaLocalizacion(locator)
            }

			@Override
            public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {
				//println "espacio ${String.valueOf(ch).substring(arg1,arg1+arg2)} acaba en ${locator.getLineNumber()} ${locator.getColumnNumber()} "
				
				situarUltimaLocalizacion(locator)
			}
			
			/*
			private String printAttributes(Attributes att)
			{
				def StringBuffer result=new StringBuffer()
				
				for(int i=0;i<att.getLength();i++)
				{
					result<<att.getQName(i)+" "+att.getValue(i)+"  "
				}	
				return result.toString()
			}*/
			
			private void situarUltimaLocalizacion(Locator loc)
			{
				ultimaLocalizacionX=loc.getColumnNumber()
				ultimaLocalizacionY=loc.getLineNumber()
			}
        }