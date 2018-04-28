package gatech.cs6300.project2.model;

import gatech.cs6300.project2.util.UtilFunctions;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class VipCustomer implements Serializable, Comparable<VipCustomer> {
	private static final long serialVersionUID = 1L;
	private final Integer id;
	private final int points;
	private final String name, phoneNumber;
	private final Date birthDate;
	private final boolean active, gold;
	
	public VipCustomer(final Integer id, final int points, final String name, final String phoneNumber, final Date birthDate, final boolean active, final boolean gold) {
		if(name == null || phoneNumber == null || birthDate == null) {
			throw new NullPointerException();
		}
		this.id = id;
		this.points = points;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.birthDate = birthDate;
		this.active = active;
		this.gold = gold;
	}
	
	public VipCustomer(final Integer id, final VipCustomer customer) {
		this(id, customer.points, customer.name, customer.phoneNumber, customer.birthDate, customer.active, customer.gold);
	}
	
	public static VipCustomer fromJson(final JSONObject json) throws JSONException, ParseException {
		Integer id;
		try {
			id = json.getInt("card_number");
		} catch(final JSONException ex) {
			id = null;
		}
		final String name = json.getString("name");
		final String dob = json.getString("dob");
		final String phone = json.getString("phone");
		final int points = json.getInt("points");
		final boolean gold = "Gold".equalsIgnoreCase(json.getString("vip_status"));
		return new VipCustomer(id, points, name, phone, UtilFunctions.parseBirthDate(dob), true, gold);
	}
	
	public JSONObject toJson() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("name", name);
		json.put("dob", UtilFunctions.formatBirthDate(birthDate));
		json.put("phone", phoneNumber);
		return json;
	}
	
	public Integer getId() {
		return id;
	}
	
	public int getPoints() {
		return points;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public Date getBirthDate() {
		return birthDate;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public boolean isGold() {
		return gold;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + birthDate.hashCode();
		result = prime * result + name.hashCode();
		result = prime * result + phoneNumber.hashCode();
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if(this == obj) {
			return true;
		} else if(obj == null || getClass() != obj.getClass()) {
			return false;
		}
		VipCustomer other = (VipCustomer) obj;
		return birthDate.equals(other.birthDate) && name.equals(other.name) && phoneNumber.equals(other.phoneNumber);
	}

	@Override
	public int compareTo(final VipCustomer another) {
		if(id == another.id) {
			return 0;
		} else if(id == null) {
			return -1;
		} else if(another.id == null) {
			return 1;
		}
		return Integer.compare(id, another.id);
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + name + ",id=" + id + ",dob=" + UtilFunctions.formatBirthDate(birthDate) + "]";
	}
}
