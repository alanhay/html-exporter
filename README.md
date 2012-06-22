html-exporter
============

Java Library to Export HTML to various Formats (XLSX, ODS, PDF, DOCX etc)
html-exporter
============

Java Library to Export HTML to Various Formats (Excel, Open Office, PDF etc).

Initial development has concentrated around  Excel (via POI) and Open Office Calc (via the ODS Toolkit)
as I couldn't find an existing HTML to (proper) spreadsheet (as opposed to csv) exporter for java.

PDF functionality should be easy enough to add (possibly even just by adding some convenience methods around
the existing xhtmlRenderer library). Word and OO Writer both open HTML documents regardless of the file extension 
so whether this is worth looking at remains to be seen.

The main purpose of the library is for use in reporting. While there are existing Java reporting frameworks 
(e.g. Jasper and BIRT) these have a fairly steep learning curve and can be painful to use: 

- lay out every report using the IDE tools
- use some cryptic scripting language to control conditional behaviour
- create a bunch of DTOs to provide the data.

If you are not using the report server (and possibly viewer) features of these applications then all they offer is 
the convenience of write once export to multiple formats.

An alternative approach then is to generate the report in HTML/CSS using you favourite easy to use templating library (Velocity, Freemarker, 
StringTemplate etc.) and use this library to export to the various formats which end-users would expect.

Example usage:

	String html = generateMyReport();

	new ExcelExporter().exportHtml(html, new File("./report.xlsx"));
	//or byte [] = new ExcelExporter().exportHtml(html);
	
	new OdsExporter().exportHtml(html, new File("./report.ods"));
	//or byte [] = new ExcelExporter().exportHtml(html);
	

An Demo is included in the source. This generates some data used to populate a Velocity template. The resulting HTML is then exported to
Excel and Open Office Calc.

The exporters allow Excel and Calc allow for producing spreadsheets with automatic formula insertion via the use of various HTML5 
compliant data-* attributes being applied to table cells.

Example:

	<!-- This cell will be added to two ranges, each of which will be the inputs to formulas -->
	<td data-group="store_${store.id}_value, area_${area.id}_pg_${group.id}_value" class="numeric $backgroundClass $bestPerformingClass">
		$store.getValueOfSalesForProductGroup($group)
    </td>   
    
    ...
	
	<!-- 
		This raw value of this cell will be replaced with a SUM function taking as input all cells added to the specified range.
		The cell is then itself added to another range which will be used by another a further function.
	-->
	<td data-group-output="store_${store.id}_value" data-group="area_${area.id}_value" class="numeric $backgroundClass" >
		$store.valueOfSales
    </td>  
    
    
Further documentation to follow.

 