package com.example.demo;

import com.example.demo.domain.TitleCase;
import com.example.demo.service.TitleCaseValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
class DemoApplicationTests {

	@Test
	public void testRusTitle() {

		TitleCaseValidator titleCaseValidator = new TitleCaseValidator(TitleCase.Type.RU);
		Assert.isTrue(titleCaseValidator.isValid("Я правильный заголовок",null),"error validator");
		Assert.isTrue(titleCaseValidator.isValid("Я правильный, заголовок,",null),"error validator");

		Assert.isTrue(!titleCaseValidator.isValid("Я правильный заголОвок почти",null),"error validator");
		Assert.isTrue(!titleCaseValidator.isValid("Я с двумя  пробелами",null),"error validator");
		Assert.isTrue(!titleCaseValidator.isValid("Я с конечным пробелом ",null),"error validator");
		Assert.isTrue(!titleCaseValidator.isValid(" Я с начальным пробелами",null),"error validator");
		Assert.isTrue(!titleCaseValidator.isValid("Я правил;ьный заголовок",null),"error validator");
		Assert.isTrue(!titleCaseValidator.isValid("Я правил\nьный заголовок",null),"error validator");
		Assert.isTrue(!titleCaseValidator.isValid("Я правил\tьный заголовок",null),"error validator");
		Assert.isTrue(!titleCaseValidator.isValid("Я Nправильный заголовок",null),"error validator");
	}

	@Test
	public void testEngTitle() {
		TitleCaseValidator titleCaseValidator = new TitleCaseValidator(TitleCase.Type.EN);
		Assert.isTrue(titleCaseValidator.isValid("I'm Right",null),"error validator");
		Assert.isTrue(titleCaseValidator.isValid("I'm Now Right",null),"error validator");
		Assert.isTrue(titleCaseValidator.isValid("I'm and Right",null),"error validator");
		Assert.isTrue(titleCaseValidator.isValid("I'm And",null),"error validator");

		Assert.isTrue(!titleCaseValidator.isValid("I'm and",null),"error validator");
		Assert.isTrue(!titleCaseValidator.isValid("I'm now Right",null),"error validator");
		Assert.isTrue(!titleCaseValidator.isValid("I'm right",null),"error validator");
		Assert.isTrue(!titleCaseValidator.isValid("I'm     No",null),"error validator");
		Assert.isTrue(!titleCaseValidator.isValid(" I'm not roght ",null),"error validator");
		Assert.isTrue(!titleCaseValidator.isValid("I'm maybъ right",null),"error validator");
		Assert.isTrue(!titleCaseValidator.isValid("I'm may\nb right",null),"error validator");
	}

	@Test
	public void testAnyTitle() {
		TitleCaseValidator titleCaseValidator = new TitleCaseValidator(TitleCase.Type.ANY);
		Assert.isTrue(titleCaseValidator.isValid("I'm Right",null),"error validator");

		Assert.isTrue(!titleCaseValidator.isValid("Я правильный заголовок or not",null),"error validator");
		Assert.isTrue(!titleCaseValidator.isValid("Я правил;ьный заголовок",null),"error validator");
		Assert.isTrue(!titleCaseValidator.isValid("Я правил\nьный заголовок",null),"error validator");
		Assert.isTrue(!titleCaseValidator.isValid("Я правил\tьный заголовок",null),"error validator");

	}


}
