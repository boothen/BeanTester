/*
 * This software is licensed under the Apache License, Version 2.0
 * (the "License") agreement; you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.force66.beantester;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.force66.beantester.testbeans.CollectionsBean;
import org.force66.beantester.testbeans.DateBean;
import org.force66.beantester.testbeans.OddTypesBean;
import org.force66.beantester.testbeans.PrimitiveTypeBean;
import org.force66.beantester.valuegens.GenericValueGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BeanTesterTest {
	
	BeanTester beanTester;

	@Before
	public void setUp() throws Exception {
		beanTester = new BeanTester();
		CallingRegistry.getCalledSet().clear();
	}

	@Test
	public void testPrimitiveTypes() throws Exception {
		beanTester.testBean(PrimitiveTypeBean.class);
		Assert.assertTrue(CallingRegistry.getCalledSet().size() == 20);
	}
	
	@Test
	public void testCollectionTypes() throws Exception {
		beanTester.testBean(CollectionsBean.class);
		Assert.assertTrue(CallingRegistry.getCalledSet().size() == 20);
	}
	
	@Test
	public void testDateTypes() throws Exception {
		beanTester.testBean(DateBean.class);
		Assert.assertTrue(CallingRegistry.getCalledSet().size() == 16);
	}
	
	@Test
	public void testOddTypes() throws Exception {
		beanTester.testBean(OddTypesBean.class);
		Assert.assertTrue(CallingRegistry.getCalledSet().size() >2);
	}
	
	@Test
	public void testAddExcludedField() throws Exception {
		beanTester.addExcludedField("fred");
		beanTester.addExcludedField("barney");
		
		BeanTesterConfiguration config = findConfiguration();
		Assert.assertTrue(config.getFieldExclusionSet().contains("fred"));
		Assert.assertTrue(config.getFieldExclusionSet().contains("barney"));
		Assert.assertTrue(config.getFieldExclusionSet().size() >= 2);
		
		try {
			beanTester.addExcludedField(null);
			Assert.fail();
		}
		catch (Exception e) {
			Assert.assertTrue(e.getMessage() != null);
			Assert.assertTrue(e.getMessage().contains("Null or blank fieldName not allowed."));
		}
	}

	private BeanTesterConfiguration findConfiguration()
			throws IllegalAccessException {
		BeanTesterConfiguration config = (BeanTesterConfiguration)
				FieldUtils.readDeclaredField(beanTester, "configuration", true);
		return config;
	}
	
	@Test
	public void testAddTestValues() throws Exception {
		beanTester.addTestValues(String.class, new Object[]{"fred", "barney"});
		BeanTesterConfiguration config = findConfiguration();
		
		GenericValueGenerator generator = (GenericValueGenerator)config.getValueGeneratorFactory()	.forClass(String.class);
		Assert.assertTrue(generator != null);
		Assert.assertTrue(generator.makeValues().length == 2);
		Assert.assertTrue(ArrayUtils.contains(generator.makeValues(), "fred"));
		Assert.assertTrue(ArrayUtils.contains(generator.makeValues(), "barney"));
	}
	
	@Test
	public void testConstructors() throws Exception {
		try {
			new BeanTester(null);
			Assert.fail();
		}
		catch (Exception e) {
			Assert.assertTrue(e.getMessage().contains("Null BeanTesterConfiguration not allowed"));
		}
	}

}
