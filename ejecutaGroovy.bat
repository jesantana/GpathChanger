cls

set rutaFile="C:\Sanitas\bravoweb\Desarrollo\BWManGen\trunkFacelets\ViewController\public_html\jsp\generales01"



set path=C:\Sanitas\datos_PC\cambiarids\groovy-1.8.4\bin;%path%
set cpcontent= "C:\Sanitas\datos_PC\cambiarids\GroovyTest\src;C:\Sanitas\datos_PC\cambiarids\GroovyTest\GroovyTest\bin"


call groovy  --classpath %cpcontent% "C:\Sanitas\datos_PC\cambiarids\GroovyTest\src\sanitas\markhand\Pruebas.groovy" %rutaFile%
pause
