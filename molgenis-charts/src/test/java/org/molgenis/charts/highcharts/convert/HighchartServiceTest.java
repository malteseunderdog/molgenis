package org.molgenis.charts.highcharts.convert;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.molgenis.MolgenisFieldTypes.FieldTypeEnum;
import org.molgenis.charts.AbstractChart.MolgenisChartType;
import org.molgenis.charts.BoxPlotChart;
import org.molgenis.charts.MolgenisAxisType;
import org.molgenis.charts.XYDataChart;
import org.molgenis.charts.data.BoxPlotSerie;
import org.molgenis.charts.data.XYData;
import org.molgenis.charts.data.XYDataSerie;
import org.molgenis.charts.highcharts.basic.AxisAlign;
import org.molgenis.charts.highcharts.basic.AxisType;
import org.molgenis.charts.highcharts.basic.ChartAlign;
import org.molgenis.charts.highcharts.basic.ChartType;
import org.molgenis.charts.highcharts.basic.Options;
import org.molgenis.charts.highcharts.basic.SeriesType;
import org.molgenis.charts.highcharts.chart.Chart;
import org.molgenis.charts.highcharts.stockchart.StockChart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.ui.Model;
import org.testng.annotations.Test;

@ContextConfiguration
public class HighchartServiceTest extends AbstractTestNGSpringContextTests
{
	@Configuration
	public static class Config
	{
		@Bean
		public HighchartService highchartsService()
		{
			return new HighchartService();
		}
		
		@Bean
		public HighchartSeriesUtil highchartSeriesUtil()
		{
			return new HighchartSeriesUtil();
		}
	}

	@Autowired
	private HighchartService highchartService;
	
	@Autowired
	private HighchartService highchartSeriesUtil;
	
	@Test
	public void renderChartInternalScatterPlot() {
		XYDataChart xYDataChart = mock(XYDataChart.class);
		doReturn(MolgenisChartType.SCATTER_CHART).when(xYDataChart).getType();
		doReturn(MolgenisAxisType.LINEAR).when(xYDataChart).getxAxisType();
		doReturn(MolgenisAxisType.LINEAR).when(xYDataChart).getyAxisType();
		assertNotNull(highchartService.renderChartInternal(xYDataChart, null));
	}
	
	@Test
	public void renderChartInternalBoxplot() {
		BoxPlotChart boxPlotChart = mock(BoxPlotChart.class);
		doReturn(MolgenisChartType.BOXPLOT_CHART).when(boxPlotChart).getType();
		assertNotNull(highchartService.renderChartInternal(boxPlotChart, null));
	}
	
	@Test
	public void renderChartInternalNull() {
		XYDataChart xYDataChart = mock(XYDataChart.class);
		doReturn(MolgenisChartType.HEAT_MAP).when(xYDataChart).getType();
		assertNull(highchartService.renderChartInternal(xYDataChart, null));
	}
	
	@Test
	public void createScatterPlotChart()
	{
		List<XYDataSerie> data = new ArrayList<XYDataSerie>();
		XYDataSerie serieOne = new XYDataSerie();
		serieOne.setAttributeXFieldTypeEnum(FieldTypeEnum.INT);
		serieOne.setAttributeXFieldTypeEnum(FieldTypeEnum.DECIMAL);
		XYData xYDataOne = new XYData(Integer.valueOf("1"), Double.valueOf("1.1"));
		XYData xYDataTwo = new XYData(Integer.valueOf("2"), Double.valueOf("2.2"));
		XYData xYDataThree = new XYData(Integer.valueOf("3"), Double.valueOf("3.3"));
		serieOne.addData(xYDataOne);
		serieOne.addData(xYDataTwo);
		serieOne.addData(xYDataThree);
		data.add(serieOne);
		XYDataChart xYDataChart = new XYDataChart(data, MolgenisAxisType.LINEAR, MolgenisAxisType.LINEAR);
		xYDataChart.setType(MolgenisChartType.SCATTER_CHART);
		xYDataChart.setxAxisLabel("xlabel");
		xYDataChart.setyAxisLabel("ylabel");
		xYDataChart.setTitle("title");
		Model model = null;

		Options options = (Options) highchartService.renderChartInternal(xYDataChart, model);

		// Options
		assertNotNull(options);

		// Chart
		assertTrue(options.getChart() instanceof Chart);

		// xAxis
		assertNotNull(options.getxAxis());
		assertFalse(options.getxAxis().isEmpty());
		assertEquals(options.getxAxis().get(0).getType(), AxisType.LINEAR.toString());
		assertEquals(options.getxAxis().get(0).getOrdinal(), Boolean.valueOf(false));
		assertEquals(options.getxAxis().get(0).getTitle().getText(), "xlabel");
		assertEquals(options.getxAxis().get(0).getTitle().getAlign(), AxisAlign.MIDDLE.toString());
		
		// yAxis
		assertNotNull(options.getyAxis());
		assertFalse(options.getyAxis().isEmpty());
		assertEquals(options.getyAxis().get(0).getType(), AxisType.LINEAR.toString());
		assertEquals(options.getyAxis().get(0).getTitle().getText(), "ylabel");
		assertEquals(options.getyAxis().get(0).getTitle().getAlign(), AxisAlign.MIDDLE.toString());

		// Title
		assertNotNull(options.getTitle());
		assertEquals(options.getTitle().getText(), "title");
		assertEquals(options.getTitle().getAlign(), ChartAlign.CENTER);

		// Legend
		assertNotNull(options.getLegend());
		assertEquals(options.getLegend().getEnabled(), Boolean.valueOf(true));
		assertEquals(options.getLegend().getAlign(), "center");
		assertEquals(options.getLegend().getLayout(), "horizontal");
		assertEquals(options.getLegend().getVerticalAlign(), "bottom");

		// Credit
		assertNotNull(options.getCredits());

		// Series
		assertNotNull(options.getSeries());
		assertFalse(options.getSeries().isEmpty());
		assertEquals(options.getSeries().get(0).getType(), SeriesType.SCATTER.toString());
		assertEquals(options.getSeries().get(0).getData().get(0), Arrays.<Object> asList(Integer.valueOf("1"), Double.valueOf("1.1")));
		assertEquals(options.getSeries().get(0).getData().get(1), Arrays.<Object> asList(Integer.valueOf("2"), Double.valueOf("2.2")));
		assertEquals(options.getSeries().get(0).getData().get(2), Arrays.<Object> asList(Integer.valueOf("3"), Double.valueOf("3.3")));
	}

	@Test
	public void createScatterPlotStockChart()
	{
		List<XYDataSerie> data = new ArrayList<XYDataSerie>();
		XYDataSerie serieOne = new XYDataSerie();
		serieOne.setAttributeXFieldTypeEnum(FieldTypeEnum.DATE);
		serieOne.setAttributeYFieldTypeEnum(FieldTypeEnum.DECIMAL);

		Calendar calOne = Calendar.getInstance();
		calOne.clear();
		calOne.set(2014, 1, 21);

		Calendar calTwo = Calendar.getInstance();
		calTwo.clear();
		calTwo.set(2014, 1, 22);

		XYData xYDataOne = new XYData(calOne.getTime(), Double.valueOf("1.1"));
		XYData xYDataTwo = new XYData(calTwo.getTime(), Double.valueOf("2.2"));
		serieOne.addData(xYDataOne);
		serieOne.addData(xYDataTwo);
		data.add(serieOne);
		XYDataChart xYDataChart = new XYDataChart(data, MolgenisAxisType.DATETIME, MolgenisAxisType.LINEAR);
		xYDataChart.setType(MolgenisChartType.SCATTER_CHART);
		xYDataChart.setxAxisLabel("xlabel");
		xYDataChart.setyAxisLabel("ylabel");
		xYDataChart.setTitle("title");
		Model model = null;

		Options options = (Options) highchartService.renderChartInternal(xYDataChart, model);

		// Options
		assertNotNull(options);

		// Chart
		assertTrue(options.getChart() instanceof StockChart);

		// xAxis
		assertNotNull(options.getxAxis());
		assertFalse(options.getxAxis().isEmpty());
		assertEquals(options.getxAxis().get(0).getType(), AxisType.DATETIME.toString());
		assertEquals(options.getxAxis().get(0).getOrdinal(), Boolean.valueOf(false));
		assertEquals(options.getxAxis().get(0).getTitle().getText(), "xlabel");
		assertEquals(options.getxAxis().get(0).getTitle().getAlign(), AxisAlign.MIDDLE.toString());

		// yAxis
		assertNotNull(options.getyAxis());
		assertFalse(options.getyAxis().isEmpty());
		assertEquals(options.getyAxis().get(0).getType(), AxisType.LINEAR.toString());
		assertEquals(options.getyAxis().get(0).getTitle().getText(), "ylabel");
		assertEquals(options.getyAxis().get(0).getTitle().getAlign(), AxisAlign.MIDDLE.toString());

		// Title
		assertNotNull(options.getTitle());
		assertEquals(options.getTitle().getText(), "title");
		assertEquals(options.getTitle().getAlign(), ChartAlign.CENTER);

		// Legend
		assertNotNull(options.getLegend());
		assertEquals(Boolean.valueOf(true), options.getLegend().getEnabled());
		assertEquals(options.getLegend().getAlign(), "center");
		assertEquals(options.getLegend().getLayout(), "horizontal");
		assertEquals(options.getLegend().getVerticalAlign(), "bottom");

		// Credit
		assertNotNull(options.getCredits());

		// Series
		assertNotNull(options.getSeries());
		assertFalse(options.getSeries().isEmpty());
		assertEquals(options.getSeries().get(0).getType(), SeriesType.LINE.toString());
		assertEquals(options.getSeries().get(0).getData().get(0), Arrays.<Object> asList(1392940800000l, Double.valueOf("1.1")));
		assertEquals(options.getSeries().get(0).getData().get(1), Arrays.<Object> asList(1393027200000l, Double.valueOf("2.2")));
	}

	@Test
	public void createBoxPlotChart()
	{
		BoxPlotChart boxPlotChart = new BoxPlotChart();

		List<BoxPlotSerie> boxPlotSeries = new ArrayList<BoxPlotSerie>();
		BoxPlotSerie boxPlotSerie = new BoxPlotSerie();
		boxPlotSerie.addData(new Double[]{ 20.5, 50.5, 100.5, 200.5, 400.5 });
		boxPlotSeries.add(boxPlotSerie);
		boxPlotChart.setBoxPlotSeries(boxPlotSeries);

		List<XYDataSerie> xYDataSeries = new ArrayList<XYDataSerie>();
		XYDataSerie xYDataSerie = new XYDataSerie();
		XYData xYDataOne = new XYData(Double.valueOf("0"), Double.valueOf("10"));
		XYData xYDataTwo = new XYData(Double.valueOf("0"), Double.valueOf("500"));
		xYDataSerie.addData(xYDataOne);
		xYDataSerie.addData(xYDataTwo);
		xYDataSeries.add(xYDataSerie);
		boxPlotChart.setxYDataSeries(xYDataSeries);

		boxPlotChart.setCategories(Arrays.asList("categoryOne"));
		boxPlotChart.setType(MolgenisChartType.BOXPLOT_CHART);
		boxPlotChart.setxLabel("xlabel");
		boxPlotChart.setyLabel("ylabel");
		boxPlotChart.setTitle("title");
		Model model = null;

		Options options = (Options) highchartService.renderChartInternal(boxPlotChart, model);

		// Options
		assertNotNull(options);

		// Chart
		assertTrue(options.getChart() instanceof Chart);
		assertEquals(ChartType.BOXPLOT.toString(), options.getChart().getType());

		// aAxis
		assertNotNull(options.getxAxis());
		assertFalse(options.getxAxis().isEmpty());
		assertNull(options.getxAxis().get(0).getType());
		assertNull(options.getxAxis().get(0).getOrdinal());
		assertEquals(options.getxAxis().get(0).getTitle().getText(), "xlabel");
		assertEquals(options.getxAxis().get(0).getTitle().getAlign(), AxisAlign.MIDDLE.toString());

		// yAxis
		assertNotNull(options.getyAxis());
		assertFalse(options.getyAxis().isEmpty());
		assertNull(options.getyAxis().get(0).getType());
		assertEquals(options.getyAxis().get(0).getTitle().getText(), "ylabel");
		assertEquals(options.getyAxis().get(0).getTitle().getAlign(), AxisAlign.MIDDLE.toString());

		// Title
		assertNotNull(options.getTitle());
		assertEquals(options.getTitle().getText(), "title");
		assertEquals(options.getTitle().getAlign(), ChartAlign.CENTER);

		// Legend
		assertNotNull(options.getLegend());
		assertEquals(Boolean.valueOf(true), options.getLegend().getEnabled());
		assertEquals(options.getLegend().getAlign(), "center");
		assertEquals(options.getLegend().getLayout(), "horizontal");
		assertEquals(options.getLegend().getVerticalAlign(), "bottom");

		// Credit
		assertNotNull(options.getCredits());

		// Series
		assertNotNull(options.getSeries());
		assertFalse(options.getSeries().isEmpty());

		assertNull(options.getSeries().get(0).getType());
		assertEquals(options.getSeries().get(0).getData().get(0), new Double[] { 20.5, 50.5, 100.5, 200.5, 400.5 });

		assertEquals(options.getSeries().get(1).getType(), SeriesType.SCATTER.toString());
		assertEquals(options.getSeries().get(1).getData().get(0), Arrays.<Object> asList(Double.valueOf("0"), Double.valueOf("10")));
		assertEquals(options.getSeries().get(1).getData().get(1), Arrays.<Object> asList(Double.valueOf("0"), Double.valueOf("500")));
	}
}
