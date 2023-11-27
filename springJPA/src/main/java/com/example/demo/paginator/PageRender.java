package com.example.demo.paginator;

import java.util.ArrayList;

import org.springframework.data.domain.Page;

public class PageRender<T> {
	private String url;
	private Page<T>page;
	private int total_paginas;
	private int pagina_actual;
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Page<T> getPage() {
		return page;
	}

	public void setPage(Page<T> page) {
		this.page = page;
	}

	public int getTotal_paginas() {
		return total_paginas;
	}

	public void setTotal_paginas(int total_paginas) {
		this.total_paginas = total_paginas;
	}

	public int getPagina_actual() {
		return pagina_actual;
	}

	public void setPagina_actual(int pagina_actual) {
		this.pagina_actual = pagina_actual;
	}

	public int getNum_elementos_x_pag() {
		return num_elementos_x_pag;
	}

	public void setNum_elementos_x_pag(int num_elementos_x_pag) {
		this.num_elementos_x_pag = num_elementos_x_pag;
	}

	public ArrayList<PageItem> getPaginas() {
		return paginas;
	}

	public void setPaginas(ArrayList<PageItem> paginas) {
		this.paginas = paginas;
	}
	private int num_elementos_x_pag;
	private ArrayList<PageItem> paginas;
	
	public PageRender(String url, Page<T> page) {
		this.url = url;
		this.page = page;
		this.paginas = new ArrayList<PageItem>();
		this.num_elementos_x_pag = page.getSize();
		this.total_paginas = page.getTotalPages();
		this.pagina_actual = page.getNumber()+1;
		
		int desde, hasta;
		if(total_paginas<=num_elementos_x_pag) {
			desde=1;
			hasta=total_paginas;
		}else {
			if(pagina_actual<=num_elementos_x_pag/2) {
				desde=1;
				hasta=num_elementos_x_pag;
			}else if(pagina_actual>(total_paginas-num_elementos_x_pag/2)){
				desde=total_paginas-num_elementos_x_pag+1;
				hasta=num_elementos_x_pag;
			}else {
				desde=pagina_actual-num_elementos_x_pag/2;
				hasta=num_elementos_x_pag;
			}
		}
		for(int i=0; i < hasta; i++) {
			paginas.add(new PageItem(desde+i,pagina_actual==desde+i));
		}
		
		
		
	}
	
	public boolean isFirst() {
		return page.isFirst();
	}
	public boolean isLast() {
		return page.isLast();
	}
	public boolean isHasNext() {
		return page.hasNext();
	}
	public boolean isHasPrevious() {
		return page.hasPrevious();
	}
	

}
