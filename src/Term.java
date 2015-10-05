public class Term {
	private String value;
	
	public Term(String s) {
		this.value = s.toLowerCase(); // Case folding - we will be creating terms only with lower case.
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Term other = (Term) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	public String getTextValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return "Term [value=" + value + "]";
	}
}