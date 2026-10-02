package org.bahmni.insurance.model;
import java.util.List;

public class CapValidation {
	private String nhisId;
	
	private String code;
	
	private String name;
	
	private Integer capQtyPeroid;
	
	private Integer capQrstPeroid;
	
	private String itemServ;
	
	private Float qtyUsed;
	
	private Float qtyRemain;
	
	public String getNhisId() {
		return nhisId;
	}
	
	public void setNhisId(String nhisId) {
		this.nhisId = nhisId;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCapQtyPeroid() {
		return capQtyPeroid;
	}

	public void setCapQtyPeroid(Integer capQtyPeroid) {
		this.capQtyPeroid = capQtyPeroid;
	}

	public Integer getCapQrstPeroid() {
		return capQrstPeroid;
	}

	public void setCapQrstPeroid(Integer capQrstPeroid) {
		this.capQrstPeroid = capQrstPeroid;
	}

	public String getItemServ() {
		return itemServ;
	}

	public void setItemServ(String itemServ) {
		this.itemServ = itemServ;
	}

	public Float getQtyUsed() {
		return qtyUsed;
	}

	public void setQtyUsed(Float qtyUsed) {
		this.qtyUsed = qtyUsed;
	}

	public Float getQtyRemain() {
		return qtyRemain;
	}

	public void setQtyRemain(Float qtyRemain) {
		this.qtyRemain = qtyRemain;
	}
}
