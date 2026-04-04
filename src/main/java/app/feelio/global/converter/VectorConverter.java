package app.feelio.global.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;

@Converter
public class VectorConverter implements AttributeConverter<float[], String> {

	@Override
	public String convertToDatabaseColumn(float[] attribute) {
		if (attribute == null) return null;
		return Arrays.toString(attribute);
	}

	@Override
	public float[] convertToEntityAttribute(String dbData) {
		if (dbData == null || dbData.isEmpty()) return null;
		String[] s = dbData.substring(1, dbData.length() - 1).split(",");
		float[] res = new float[s.length];
		for (int i = 0; i < s.length; i++) {
			res[i] = Float.parseFloat(s[i].trim());
		}
		return res;
	}
}