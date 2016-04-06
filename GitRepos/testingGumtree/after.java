package com.jop;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jop.content.matcher.ContentMatcher;
import com.jop.content.matcher.SimpleMatcher;

public class Example {

	private static final String pattern1 = "Angel";
	private static final String pattern2 = "Szlak";

	public static void main(final String[] args) {

		final WebDriver driver = new HtmlUnitDriver();

		driver.get("http://www.gumtree.pl/fp-domy-i-mieszkania-do-wynajecia/krakow/c9008l3200208?A_NumberRooms=2&AdType=2");

		final List<WebElement> elements = driver.findElements(By
				.className("adLinkSB"));

		final ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "applicationContext.xml" });

		final ContentMatcher matcher = (SimpleMatcher) context
				.getBean("simpleMatcher");

		matcher.keyword(pattern1).keyword(pattern2).keyword("Krakowska");

		System.out.println("Just testing add");

		int i = 1;
		for (final WebElement el : elements) {
			System.out.println(i++);
			process(e, matcher);
		}

		System.out.println("Finish");
	}

	private static void process(final WebElement e, final ContentMatcher matcher) {
		// <a
		// href="http://www.gumtree.pl/cp-domy-i-mieszkania-do-wynajecia/krakow/ladne-dwupokojowe-mieszkanie-w-centrum-546674434?featuredAd=true"
		// class="adLinkSB">
		final String rawAddress = e.toString();

		final int from = rawAddress.indexOf("http");
		final int to = rawAddress.indexOf('"', from);

		final String address = rawAddress.substring(from, to);

		final WebDriver driver = new HtmlUnitDriver();

		driver.get(address);

		final String html_content = driver.getPageSource();

		if (matcher.match(html_content)) {
			System.out.println(address);
		}

	}
}
