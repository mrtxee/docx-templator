package ru.fabit.docxtemplate.domain.templates.models;

import static ru.fabit.docxtemplate.domain.templates.models.validators.Validators.requireNotNull;

import com.deepoove.poi.data.ByteArrayPictureRenderData;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.style.PictureStyle;
import com.deepoove.poi.xwpf.WidthScalePattern;
import org.apache.commons.codec.binary.Base64;

public class TagDataValuePicture extends TagDataValue {
  final static String PNG_BASE64_STARTS_WITH = "iVBORw0KGgo";
  final static String JPG_BASE64_STARTS_WITH = "/9j/";

  private final PictureRenderData value;

  TagDataValuePicture(String name, String value) {
    super(name.replaceFirst("@", ""));
    requireNotNull(value, "templateTags[].value must not null. templateTags[].name=%s".formatted(name));
    if (!Base64.isBase64(value)) {
      throw new IllegalArgumentException(String.format("Base64 value expected for %s", name));
    }
    if (!value.startsWith(PNG_BASE64_STARTS_WITH) && !value.startsWith(JPG_BASE64_STARTS_WITH)){
      throw new IllegalArgumentException(String.format("Picture tag %s should be PNG or JPG formatted only", name));
    }
    PictureStyle pictureStyle = new PictureStyle();
    pictureStyle.setScalePattern(WidthScalePattern.FIT);
    PictureRenderData pictureRenderData = new ByteArrayPictureRenderData(Base64.decodeBase64(value));
    pictureRenderData.setPictureStyle(pictureStyle);
    this.value = pictureRenderData;
  }

  public PictureRenderData getValue() {
    return value;
  }
}