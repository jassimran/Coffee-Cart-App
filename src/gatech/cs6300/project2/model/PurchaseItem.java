package gatech.cs6300.project2.model;

import java.text.NumberFormat;

public class PurchaseItem 
{
	private Integer id;
	private String name;
	private Double price;
	
	public PurchaseItem()
	{
		
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String toString()
	{
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		String moneyString = formatter.format(price);
		return name + ": " + moneyString;
	}
	
}
