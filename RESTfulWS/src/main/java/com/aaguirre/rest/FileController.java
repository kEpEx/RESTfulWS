package com.aaguirre.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileController {
	
	@RequestMapping(value = "/file", method = RequestMethod.GET)
	@ResponseBody
	public String file()
	{
		String path = request.getParameter("path");
		// Genera ArrayList para guardar errores 
		ArrayList<String> errores = new ArrayList<String>();
		// 4(e) Cargar el resource bundle del atributo de la sessión "locale"
		Locale locale = new Locale((String) request.getSession().getAttribute("locale")); // Escribe aqui tu codigo
		ResourceBundle bundle = ResourceBundle.getBundle("me.jmll.i18n.app", locale); // Escribe aqui tu codigo
		
		LOGGER.log(Level.WARNING, locale.getLanguage());
		// Validar si el path tiene contenido
		if (path != null){
			// Mediante java.nio.Path, 
			// crea un objeto basado en el parámetro path
			Path dir = Paths.get(path);
			
			// Crea un objeto de tipo List que almacene objetos
			// java.nio.Path y sea ArrayList.
			List<Path> paths = new ArrayList<>();
			
			// Valida que exista el objeto Path creado
			if (Files.exists(dir) && Files.isDirectory(dir)){
				NIO2RecursiveDir.walkDir(dir, paths);
			} else {
				errores.add(String.format(bundle.getString("app.download.error"), path));
				// asigna el objeto errores en el 
				// atributo de la request con el nombre "errores"
				request.setAttribute("errores", errores);
				request.getRequestDispatcher("/Admin.do").forward(request, response);
			}
			// Agregar los resultados como atributo en el request
			request.setAttribute("path", path);
			request.setAttribute("paths", paths);
			
			// Obtiene Requestdispatcher a DirView.jsp
			// y reenvía con el método forward
			request.getRequestDispatcher("/WEB-INF/views/DirView.jsp").forward(request, response);
		} else {
			errores.add(bundle.getString("app.download.error"));
			request.setAttribute("errores", errores);
			LOGGER.log(Level.WARNING, "Parámetro PATH requerido." );
			request.getRequestDispatcher("/Admin.do").forward(request, response);
		}
	}
	
	@RequestMapping(value = "/file", method = RequestMethod.POST)
	@ResponseBody
	public String file_post()
	{
		ArrayList<String> errores = new ArrayList<String>();
    	// 4(e) Cargar el resource bundle del atributo de la sessión "locale"
    	Locale locale = // Escribe aquí tu código
		ResourceBundle bundle = // Escribe aquí tu código
        // Obtener el parámetro fileName
        String fileName = request.getParameter("archivo");
        // Validar que fileName viene en la solicitud
        if (fileName != null) {
            // Obtener el contexto del servlet en la variable servletContext
            ServletContext servletContext = request.getServletContext();
            try {
                // Crea un objeto File con el path solicitado
                File fileDownload = new File(fileName);
                if (fileDownload.exists()) {
                    // Crear instancias de FileInputStrem a partir del archivo a descargar
                    //    y OutputStream a partir del objeto response
                    try (FileInputStream fileInputStream = new FileInputStream(fileDownload);
                            OutputStream outputStream = response.getOutputStream();) {

                        // Obtener MimeType del Archivo
                        String mimeType = servletContext.getMimeType(fileName);
                        if (mimeType == null)
                            mimeType = "application/octet-stream";
                        LOGGER.log(Level.INFO, "MimeType identificado: {0}", new Object[] { mimeType });
                        
                        // Crear response: MimeType
                        response.setContentType(mimeType);
                        response.setContentLength((int) fileDownload.length());
                        
                        // Crear response: Headers
                        response.setHeader("Content-Disposition",
                                String.format("attachment; filename=\"%s\";", fileDownload.getName()));
                        LOGGER.log(Level.INFO, "Iniciando transferencia de {0} {1} Bytes a {2}", new Object[] {
                                fileDownload.getName(), fileDownload.length(), request.getRemoteAddr() });
                        // Transmite datos
                        byte[] buffer = new byte[4096];
                        int bytesRead = -1;

                        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                        LOGGER.log(Level.INFO, "Transferencia completada {0}", fileDownload.getName());
                    }

                } else {
                    errores.add(String.format(/*bundle.getString()*/, fileName));
                    request.setAttribute("errores", errores);
        			request.getRequestDispatcher("/Admin.do").forward(request, response);
                }

            } catch (Exception ex) {
                errores.add(String.format(/*bundle.getString()*/, ex));
                LOGGER.log(Level.SEVERE, "Problemas durante la descarga de archivo. Error: {0}",
                        new Object[] { ex.getMessage() });
                request.setAttribute("errores", errores);
    			request.getRequestDispatcher("/Admin.do").forward(request, response);
            }
        } else {
        	errores.add(String.format(/*bundle.getString()*/));
            request.setAttribute("errores", errores);
			request.getRequestDispatcher("/Admin.do").forward(request, response);
        }
	}
	
	@RequestMapping(value = "/file", method = RequestMethod.OPTIONS)
	@ResponseBody
	public String file_options()
	{
		String path = request.getParameter("path");
		// Genera ArrayList para guardar errores 
		ArrayList<String> errores = new ArrayList<String>();
		// 4(e) Cargar el resource bundle del atributo de la sessión "locale"
		Locale locale = new Locale((String) request.getSession().getAttribute("locale")); // Escribe aqui tu codigo
		ResourceBundle bundle = ResourceBundle.getBundle("me.jmll.i18n.app", locale); // Escribe aqui tu codigo
		
		LOGGER.log(Level.WARNING, locale.getLanguage());
		// Validar si el path tiene contenido
		if (path != null){
			// Mediante java.nio.Path, 
			// crea un objeto basado en el parámetro path
			Path dir = Paths.get(path);
			
			// Crea un objeto de tipo List que almacene objetos
			// java.nio.Path y sea ArrayList.
			List<Path> paths = new ArrayList<>();
			
			// Valida que exista el objeto Path creado
			if (Files.exists(dir) && Files.isDirectory(dir)){
				NIO2RecursiveDir.walkDir(dir, paths);
			} else {
				errores.add(String.format(bundle.getString("app.download.error"), path));
				// asigna el objeto errores en el 
				// atributo de la request con el nombre "errores"
				request.setAttribute("errores", errores);
				request.getRequestDispatcher("/Admin.do").forward(request, response);
			}
			// Agregar los resultados como atributo en el request
			request.setAttribute("path", path);
			request.setAttribute("paths", paths);
			
			// Obtiene Requestdispatcher a DirView.jsp
			// y reenvía con el método forward
			request.getRequestDispatcher("/WEB-INF/views/DirView.jsp").forward(request, response);
		} else {
			errores.add(bundle.getString("app.download.error"));
			request.setAttribute("errores", errores);
			LOGGER.log(Level.WARNING, "Parámetro PATH requerido." );
			request.getRequestDispatcher("/Admin.do").forward(request, response);
		}
	}
	
	
	

	
}
