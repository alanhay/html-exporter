# HTML-EXPORTER


Java Library to Export CSS styled HTML to various Formats (XLSX, ODS, PDF)

Sample Reports generated using the library:

[Open Office ODS Spreadsheet](http://tinyurl.com/nhq5mu9)
[MS Excel XLSX Spreadsheet](http://tinyurl.com/pbnao9u)
[PDF Document](http://tinyurl.com/o2hk9l7)


Initial development has concentrated around  Excel (via POI) and Open Office Calc (via the ODS Toolkit) as I couldn't find an existing HTML to (proper) spreadsheet (as opposed to csv) exporter for java.

For PDF generation this library simply provides some convenience methods around the existing [xhtmlRenderer/flying saucer](https://github.com/flyingsaucerproject/flyingsaucer) library. 

#### Maven

```xml
<dependency>
    <groupId>io.github.alanhay</groupId>
    <artifactId>html-exporter</artifactId>
    <version>0.5.4</version>
</dependency>
```

## Usage

The main purpose of the library is for use in reporting. While there are existing Java reporting frameworks 
(e.g. Jasper and BIRT) these have a fairly steep learning curve and can be painful to use: 

* lay out every report using the IDE tools
* use some cryptic scripting language to control conditional behaviour
* create a bunch of DTOs to provide the data.

If you are not using the report server (and possibly viewer) features of these reporting frameworks then 
the main feature they offer is the convenience of write once export to multiple formats.

An alternative approach then is to generate the report in HTML/CSS using you favourite easy to use templating library (Velocity, Freemarker,  StringTemplate etc.) and use this library to export to the various formats which end-users would expect.

Example usage:

```java
String html = generateMyReport();

new ExcelExporter().exportHtml(html, new File("./report.xlsx"));
//or byte [] = new ExcelExporter().exportHtml(html);

new OdsExporter().exportHtml(html, new File("./report.ods"));
//or byte [] = new ExcelExporter().exportHtml(html);

new PdfExporter().exportHtml(html, new File("./report.pdf"));
//or byte [] = new ExcelExporter().exportHtml(html);
```


The reports linked to above were generated via a demo application which is included in the source. This generates some data used to populate a Velocity template. The resulting HTML is then exported to Excel, PDF and Open Office Calc.

Run the below to execute the demo:

[uk.co.certait.htmlexporter.demo.ReportGenerator](https://github.com/alanhay/html-exporter/blob/master/src/main/java/uk/co/certait/htmlexporter/demo/ReportGenerator.java)

## Features

* [CSS support](#CSS-Support)
* [Formula support](#Formula-Support)
* [Cell comments](#Cell-Comments)
* [Freeze panes](#Freeze-Panes)
* [Merged cells](#Merged-Cells)
* [Multiple worksheets](#Multiple-Worksheets)



### CSS Support

Styles can be applied via a <style/> block, via a class attribute <td class="classA classB..."> or via an in line style attribute <td style="background-color: orange; color: #ff00ff;">

The normal order of precedence will be applied i.e. inline overrides class declarations overrides global declarations.

Inheritance should be handled as expected e.g. specifying a background color on a table or a row within that table then that property should be inherited by child <td/> elements (unless overridden by a more specific rule)

The following CSS attributes will be parsed by the Excel exporter. **The shorthand versions should be handled as expected** e.g.

```css
{border: thick solid red;}
```

* font-family
* font-size
* font-weight
* font-style
* text-decoration
* text-align
* vertical-align
* color 
* background-color
* border-color
* border-top-color
* border-bottom-color
* border-left-color
* border-right-color
* border-style
* border-top-style
* border-bottom-style
* border-left-style
* border-right-style
* border-width
* border-top-width
* border-bottom-width
* border-left-width
* border-right-width

##### CSS Notes:
* Colors can be specified as literals where there is a corresponding Java constant e.g. red, black orange etc.
* Where colors are specified as hex values then the long format must be used e.g. #ff0000 rather than #f00
* the underlying Excel library (Apache POI) does not support setting arbitrary border widths e.g. 5px. Border widths must then be specified as 'thin', 'medium' or 'thick'
* supported border styles are: solid, dotted, dashed, double. Widths are only applied to 'solid' style.


### Formula Support

The ODS and Excel exporters allow for producing spreadsheets with automatic formula insertion via the use of various data-attributes being applied to table cells. The sample spreadsheets linked to above demonstrate this functionality.

Example:

```html
<!-- This cell will, via the data-group attribute,  be added to two ranges, each of which will be the inputs to formulas -->
<td data-group="store_Dumfries_2_value, region_1_1_pg_5_value" class="numeric">
	486
</td>      

...

<!-- 
	This raw value of this cell will, via the data-group-output attribute, be replaced with a SUM function taking as input all cells added to the specified range.
	The cell is then itself added to another range which will be used by a further function.
-->
<td class="subTotal numeric" data-group-output="region_1_1_pg_6_count" data-group="area_1_pg_6_count">
	32
</td>
```

### Cell Comments

Excel cell comments can be created as below. The dimension attribute (specified in columns and rows is optional and defauls to 3 columns and 1 row if not specified) 

```html
<td data-cell-comment="An Excel Comment" data-cell-comment-dimension="4,2">some value</td>
```

### Freeze Panes

A freeze pane can be created as below i.e. to set the first 2 columns as a freeze pane then specify as below in the cell a column A, row 3:

```html
<td data-freeze-pane-cell="true">some value</td>
```

### Merged Cells

Merged regions can be created using the standars html rowspan and colspan attributes:

```html
<td rowspan="3" colspan="3">some value</td>
```

### Multiple Worksheets

For output to multiple worksheets, use the data-new-sheet attribute. For specifying the worksheet name use the data-sheet-name attribute. e.g.

```html
<!--multiple tables output to separate sheets-->
<table data-sheet-name="Table 1">
	<!-- table data-->
</table>
<table data-new-sheet="true" data-sheet-name="Table 2">
	<!-- table data-->
</table>
```

### Additional Processing Instructions

#### Handling dates 

To create a date/time cell use the data-date-cell-format attribute in order that the text value of the table cell can be parsed to a Java data e.g 

```html
<td data-date-cell-format="dd/MM/yy HH:mm:ss">01-01-2022 13:00:00</td>
```

#### Forcing Text output 

To force the cell type to be text regardless of the underlying type i.e. prevent excel trying to interpret cell values as some other type e.g. 

```html
<td data-text-cell="true">13.54</td>
```