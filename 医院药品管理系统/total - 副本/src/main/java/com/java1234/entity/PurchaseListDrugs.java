package com.java1234.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/*
 * 进货单药品实体
 * @author java1234 AT
 *
 */
@Entity
@Table(name="t_purchase_list_drugs")
public class PurchaseListDrugs {

	@Id
	@GeneratedValue
	private Integer id; // 编号
	
	@ManyToOne
	@JoinColumn(name="purchaseListId")
	private PurchaseList purchaseList; // 进货单

	@Column(length=50)
	private String code; // 药品编码
	
	
	@Column(length=50)
	private String name; // 药品名称
	
	@Column(length=50)
	private String model; // 药品型号
	
	@ManyToOne
	@JoinColumn(name="typeId")
	private DrugsType type; // 药品类别
	
	@Transient
	private Integer typeId; // 类别id 
	
	private Integer drugsId; // 药品id
	
	@Column(length=10)
	private String unit; // 药品单位
	
	private float price; // 单价
	
	private int num; // 数量
	
	private float total; // 总价

	
	@Transient
	private String codeOrName; // 查询用到  根据药品编码或者药品名称查询

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public DrugsType getType() {
		return type;
	}

	public void setType(DrugsType type) {
		this.type = type;
	}
	
	
	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}
	
	
	public PurchaseList getPurchaseList() {
		return purchaseList;
	}

	public void setPurchaseList(PurchaseList purchaseList) {
		this.purchaseList = purchaseList;
	}
	
	

	public Integer getDrugsId() {
		return drugsId;
	}

	public void setDrugsId(Integer drugsId) {
		this.drugsId = drugsId;
	}
	
	

	public String getCodeOrName() {
		return codeOrName;
	}

	public void setCodeOrName(String codeOrName) {
		this.codeOrName = codeOrName;
	}

	@Override
	public String toString() {
		return "[id=" + id + ", code=" + code + ", name=" + name + ", model=" + model + ", type="
				+ type + ", unit=" + unit + ", price=" + price + ", num=" + num + ", total=" + total + "]";
	}

	
	
}
