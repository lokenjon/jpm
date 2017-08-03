package com.jl.technicaltest.jpmorgan;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.jl.technicaltest.jpmorgan.model.Report;

import junit.framework.TestCase;

public class ReportTest extends TestCase{

	public void testMapSort() throws Exception{
		Report report = new Report();
		Map<String, Float> map = new HashMap<String, Float>();
		map.put("aaa", 5f);
		map.put("bbb", 1f);
		map.put("ccc", 10f);
		map.put("ddd", -1f);
		map.put("eee", 0f);

		report.setEntity2IncomingUSD(map);
		
		Map<String, Float> res = report.getEntity2IncomingUSDRanked();
		Iterator<String> it = res.keySet().iterator();
		assertEquals("ccc", it.next());
		assertEquals("aaa", it.next());
		assertEquals("bbb", it.next());
		assertEquals("eee", it.next());
		assertEquals("ddd", it.next());
    }
}
