package com.claymus.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.claymus.data.transfer.Page;
import com.claymus.data.transfer.PageContent;
import com.claymus.data.transfer.PageLayout;
import com.claymus.data.transfer.WebsiteLayout;
import com.claymus.data.transfer.WebsiteWidget;
import com.claymus.module.pagecontent.PageContentProcessor;
import com.claymus.module.pagecontent.PageContentRegistry;
import com.claymus.module.pagecontent.html.HtmlContentFactory;
import com.claymus.module.websitewidget.WebsiteWidgetProcessor;
import com.claymus.module.websitewidget.WebsiteWidgetRegistry;
import com.claymus.module.websitewidget.header.HeaderFactory;
import com.claymus.module.websitewidget.user.UserInfoFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@SuppressWarnings("serial")
public class ClaymusMain extends HttpServlet {

	static {
		PageContentRegistry.register( HtmlContentFactory.class );

		WebsiteWidgetRegistry.register( HeaderFactory.class );
		WebsiteWidgetRegistry.register( UserInfoFactory.class );
	}
	
	@Override
	public void doGet(
			HttpServletRequest request,
			HttpServletResponse response ) throws IOException {

		Page page = getPage();
		List<PageContent> pageContentList = getPageContentList( request );
		List<WebsiteWidget> websiteWidgetList = getWebsiteWidgetList( request );
		PageLayout pageLayout = getPageLayout();
		WebsiteLayout websiteLayout = getWebsiteLayout();
		
		PrintWriter out = response.getWriter();
		renderPage( page, pageContentList, websiteWidgetList, pageLayout, websiteLayout, out );
		out.close();
	}
	
	private void renderPage(
			Page page,
			List<PageContent> pageContentList,
			List<WebsiteWidget> websiteWidgetList,
			PageLayout pageLayout,
			WebsiteLayout websiteLayout,
			PrintWriter out
			) {

		/*
		 * The DOCTYPE declaration below will set the browser's rendering engine
		 * into "Standards Mode". Replacing this declaration with a "Quirks Mode"
		 * doctype is not supported.
		 */
		out.println( "<!doctype html>" );
		
		out.println( "<html>" );
		out.println( "<head>" );
		
		
		out.println( "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"> ");
		out.println( "<title>" + page.getTitle() + "</title>" );
		  
		out.println( "</head>" );
		out.println( "<body>" );

		/*
		 * OPTIONAL: include this if you want history support
		 */
		out.println( "<iframe src=\"javascript:''\" id=\"__gwt_historyFrame\" tabIndex='-1' style=\"position:absolute;width:0;height:0;border:0\"></iframe>" );
		    
		/*
		 * RECOMMENDED: if your web app will not function without JavaScript enabled
		 */
		out.println( "<noscript>" );
		out.println( "<div style=\"width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif\"> ");
		out.println( "Your web browser must have JavaScript enabled in order for this application to display correctly." );
		out.println( "</div>" );
		out.println( "</noscript>" );
		
		
		List<String> websiteWidgetHtmlList = new LinkedList<>();
		for( WebsiteWidget websiteWidget : websiteWidgetList ) {
			@SuppressWarnings("rawtypes")
			WebsiteWidgetProcessor websiteWidgetProcessor = 
					WebsiteWidgetRegistry.getWebsiteWidgetProcessor( websiteWidget.getClass() );
			@SuppressWarnings("unchecked")
			String websiteWidgetHtml = websiteWidgetProcessor.getHtml( websiteWidget );
			websiteWidgetHtmlList.add( websiteWidgetHtml );
		}
		
		List<String> pageContentHtmlList = new LinkedList<>();
		for( PageContent pageContent : pageContentList ) {
			@SuppressWarnings("rawtypes")
			PageContentProcessor pageContentProcessor =
					PageContentRegistry.getPageContentProcessor( pageContent.getClass() );
			@SuppressWarnings("unchecked")
			String pageContentHtml = pageContentProcessor.getHtml( pageContent );
			pageContentHtmlList.add( pageContentHtml );
		}
		
		Map<String, Object> input = new HashMap<String, Object>();
		input.put( "websiteWidgetList", websiteWidgetHtmlList );
		input.put( "pageContentList", pageContentHtmlList );
		
		try {
			Template template = new Template( null, websiteLayout.getTemplate(), new Configuration() );
			template.process( input, out );

			template = new Template( null, pageLayout.getTemplate(), new Configuration() );
			template.process( input, out );
		} catch ( IOException | TemplateException e ) {
			e.printStackTrace();
		}
		

		out.println( "</body>" );
		out.println( "</html>" );
	}
	
	private Page getPage() {
		
		return new Page() {

			@Override
			public Long getId() {
				return null;
			}

			@Override
			public String getUri() {
				return null;
			}

			@Override
			public void setUri( String uri ) { }

			@Override
			public String getTitle() {
				return null;
			}

			@Override
			public void setTitle( String title ) { }

			@Override
			public Long getLayoutId() {
				return null;
			}

			@Override
			public void setLayout( Long layoutId ) { }

			@Override
			public Date getCreationDate() {
				return null;
			}

			@Override
			public void setCreationDate( Date creationDate ) { }
		
		};
		
	}
	
	protected List<PageContent> getPageContentList(
			HttpServletRequest request ) {
		
		return new LinkedList<>();
	}

	protected List<WebsiteWidget> getWebsiteWidgetList(
			HttpServletRequest request ) {
		
		return new LinkedList<>();
	}

	private PageLayout getPageLayout() {
		
		return new PageLayout() {

			@Override
			public Long getId() {
				return null;
			}

			@Override
			public String getName() {
				return null;
			}

			@Override
			public void setName( String name ) { }

			@Override
			public String getTemplate() {
				return "<#list pageContentList as pageContent>"
						+ "${pageContent}"
						+ "</#list>";
			}

			@Override
			public void setTemplate( String template ) { }
			
		};
		
	}
	
	private WebsiteLayout getWebsiteLayout() {
		
		return new WebsiteLayout() {

			@Override
			public Long getId() {
				return null;
			}

			@Override
			public String getName() {
				return null;
			}

			@Override
			public void setName( String name ) { }

			@Override
			public String getTemplate() {
				return "<#list websiteWidgetList as websiteWidget>"
						+ "${websiteWidget}"
						+ "</#list>";
			}

			@Override
			public void setTemplate( String template ) { }
			
		};
		
	}

}