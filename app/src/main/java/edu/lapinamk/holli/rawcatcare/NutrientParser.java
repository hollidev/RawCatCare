package edu.lapinamk.holli.rawcatcare;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;

public class NutrientParser implements XmlPullParser, XmlResourceParser
{
    private final static String TAG_CALORIES = "calories";
    private final static String TAG_PROTEINS = "protein";
    private final static String TAG_FATS = "fat";
    private final static String TAG_CARBS = "carbohydrates";
    private final static String TAG_PHOSPHORUS = "phosphorus";
    private final static String TAG_CALCIUM = "calcium";
    private final static String TAG_VITAMIN_A = "A";
    private final static String TAG_VITAMIN_B1 = "B1";
    private final static String TAG_VITAMIN_B2 = "B2";
    private final static String TAG_VITAMIN_B3 = "B3";
    private final static String TAG_VITAMIN_B5 = "B5";
    private final static String TAG_VITAMIN_B6 = "B6";
    private final static String TAG_VITAMIN_B7 = "B7";
    private final static String TAG_VITAMIN_B9 = "B9";
    private final static String TAG_VITAMIN_B12 = "B12";
    private final static String TAG_VITAMIN_C = "C";
    private final static String TAG_VITAMIN_D = "D";
    private final static String TAG_VITAMIN_E = "E";
    private final static String TAG_VITAMIN_K = "K";
    private final static String TAG_CHLORIDE = "chloride";
    private final static String TAG_COPPER = "copper";
    private final static String TAG_IODINE = "iodine";
    private final static String TAG_IRON = "iron";
    private final static String TAG_MAGNESIUM = "magnesium";
    private final static String TAG_MANGANESE = "manganese";
    private final static String TAG_SELENIUM = "selenium";
    private final static String TAG_SODIUM = "sodium";
    private final static String TAG_ZINC = "zinc";
    private final static String TAG_OMEGA3 = "omega3";
    private final static String TAG_OMEGA6 = "omega6";
    private final static String TAG_POTASSIUM = "potassium";

    public static HashMap<String, String> parseNutrients(HashMap<String, String> values, Context context, String meat, String part) throws IOException, XmlPullParserException
    {
        Resources r = context.getResources();
        XmlResourceParser xrp;

        if (meat.compareToIgnoreCase("mush") == 0)
        {
            // formatting the tags to be searched
            meat = part.substring(0, part.indexOf(" "));
            part = part.substring(part.indexOf(" ") + 1);

            // initializing the parser with the XML file
            xrp = r.getXml(R.xml.mush_foods);
        }
        else
        {
            xrp = r.getXml(R.xml.foods);
        }

        // beginning to parse
        xrp.next();

        int eventType = xrp.getEventType();

        // parsing until the document has ended
        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            if (eventType == XmlPullParser.START_TAG && 0 == meat.compareToIgnoreCase(xrp.getName()))
            {
                // if the start tag of an element is the selected meat, the parser begins looking for the part tag
                values = parsePart(values, xrp, part);
            }
            eventType = xrp.next();
        }

        return values;
    }

    private static HashMap<String, String> parsePart(HashMap<String, String> values, XmlResourceParser xrp, String part) throws IOException, XmlPullParserException
    {
        xrp.next();
        int eventType = xrp.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            try
            {
                switch (xrp.getName())
                {
                    /*
                    this switch statement puts the name and value of each found nutrient into the hashmap
                    can also be done in a loop
                     */
                    case TAG_CALORIES:
                        values.put(TAG_CALORIES, xrp.nextText());
                        break;
                    case TAG_CARBS:
                        values.put(TAG_CARBS, xrp.nextText());
                        break;
                    case TAG_COPPER:
                        values.put(TAG_COPPER, xrp.nextText());
                        break;
                    case TAG_FATS:
                        values.put(TAG_FATS, xrp.nextText());
                        break;
                    case TAG_PROTEINS:
                        values.put(TAG_PROTEINS, xrp.nextText());
                        break;
                    case TAG_PHOSPHORUS:
                        values.put(TAG_PHOSPHORUS, xrp.nextText());
                        break;
                    case TAG_POTASSIUM:
                        values.put(TAG_POTASSIUM, xrp.nextText());
                        break;
                    case TAG_VITAMIN_B1:
                        values.put(TAG_VITAMIN_B1, xrp.nextText());
                        break;
                    case TAG_CALCIUM:
                        values.put(TAG_CALCIUM, xrp.nextText());
                        break;
                    case TAG_VITAMIN_K:
                        values.put(TAG_VITAMIN_K, xrp.nextText());
                        break;
                    case TAG_SELENIUM:
                        values.put(TAG_SELENIUM, xrp.nextText());
                        break;
                    case TAG_IRON:
                        values.put(TAG_IRON, xrp.nextText());
                        break;
                    case TAG_IODINE:
                        values.put(TAG_IODINE, xrp.nextText());
                        break;
                    case TAG_MANGANESE:
                        values.put(TAG_MANGANESE, xrp.nextText());
                        break;
                    case TAG_ZINC:
                        values.put(TAG_ZINC, xrp.nextText());
                        break;
                    case TAG_VITAMIN_B5:
                        values.put(TAG_VITAMIN_B5, xrp.nextText());
                        break;
                    case TAG_VITAMIN_B6:
                        values.put(TAG_VITAMIN_B5, xrp.nextText());
                        break;
                    case TAG_VITAMIN_E:
                        values.put(TAG_VITAMIN_E, xrp.nextText());
                        break;
                    case TAG_VITAMIN_B7:
                        values.put(TAG_VITAMIN_B7, xrp.nextText());
                        break;
                    case TAG_VITAMIN_B9:
                        values.put(TAG_VITAMIN_B9, xrp.nextText());
                        break;
                    case TAG_VITAMIN_B2:
                        values.put(TAG_VITAMIN_B2, xrp.nextText());
                        break;
                    case TAG_VITAMIN_D:
                        values.put(TAG_VITAMIN_D, xrp.nextText());
                        break;
                    case TAG_VITAMIN_A:
                        values.put(TAG_VITAMIN_A, xrp.nextText());
                        break;
                    case TAG_VITAMIN_B3:
                        values.put(TAG_VITAMIN_B3, xrp.nextText());
                        break;
                    case TAG_VITAMIN_B12:
                        values.put(TAG_VITAMIN_B12, xrp.nextText());
                        break;
                    case TAG_MAGNESIUM:
                        values.put(TAG_MAGNESIUM, xrp.nextText());
                        break;
                    case TAG_SODIUM:
                        values.put(TAG_SODIUM, xrp.nextText());
                        break;
                    case TAG_VITAMIN_C:
                        values.put(TAG_VITAMIN_C, xrp.nextText());
                        break;
                    case TAG_CHLORIDE:
                        values.put(TAG_CHLORIDE, xrp.nextText());
                        break;
                }
            } catch (NullPointerException e) // nextText() will throw this if no value is present
            {
                xrp.next();
            }

            xrp.next();

            if (xrp.getName() != null)
            {
                // when the ending tag of the part element has been reached, stop execution
                if (xrp.getEventType() == XmlPullParser.END_TAG && 0 == part.compareToIgnoreCase(xrp.getName()))
                {
                    break;
                }
            }

        }

        return values;
    }

    @Override
    public void setFeature(String name, boolean state) throws XmlPullParserException
    {

    }

    @Override
    public boolean getFeature(String name)
    {
        return false;
    }

    @Override
    public void setProperty(String name, Object value) throws XmlPullParserException
    {

    }

    @Override
    public Object getProperty(String name)
    {
        return null;
    }

    @Override
    public void setInput(Reader in) throws XmlPullParserException
    {

    }

    @Override
    public void setInput(InputStream inputStream, String inputEncoding) throws XmlPullParserException
    {

    }

    @Override
    public String getInputEncoding()
    {
        return null;
    }

    @Override
    public void defineEntityReplacementText(String entityName, String replacementText) throws XmlPullParserException
    {

    }

    @Override
    public int getNamespaceCount(int depth) throws XmlPullParserException
    {
        return 0;
    }

    @Override
    public String getNamespacePrefix(int pos) throws XmlPullParserException
    {
        return null;
    }

    @Override
    public String getNamespaceUri(int pos) throws XmlPullParserException
    {
        return null;
    }

    @Override
    public String getNamespace(String prefix)
    {
        return null;
    }

    @Override
    public int getDepth()
    {
        return 0;
    }

    @Override
    public String getPositionDescription()
    {
        return null;
    }

    @Override
    public int getAttributeNameResource(int index)
    {
        return 0;
    }

    @Override
    public int getAttributeListValue(String namespace, String attribute, String[] options, int defaultValue)
    {
        return 0;
    }

    @Override
    public boolean getAttributeBooleanValue(String namespace, String attribute, boolean defaultValue)
    {
        return false;
    }

    @Override
    public int getAttributeResourceValue(String namespace, String attribute, int defaultValue)
    {
        return 0;
    }

    @Override
    public int getAttributeIntValue(String namespace, String attribute, int defaultValue)
    {
        return 0;
    }

    @Override
    public int getAttributeUnsignedIntValue(String namespace, String attribute, int defaultValue)
    {
        return 0;
    }

    @Override
    public float getAttributeFloatValue(String namespace, String attribute, float defaultValue)
    {
        return 0;
    }

    @Override
    public int getAttributeListValue(int index, String[] options, int defaultValue)
    {
        return 0;
    }

    @Override
    public boolean getAttributeBooleanValue(int index, boolean defaultValue)
    {
        return false;
    }

    @Override
    public int getAttributeResourceValue(int index, int defaultValue)
    {
        return 0;
    }

    @Override
    public int getAttributeIntValue(int index, int defaultValue)
    {
        return 0;
    }

    @Override
    public int getAttributeUnsignedIntValue(int index, int defaultValue)
    {
        return 0;
    }

    @Override
    public float getAttributeFloatValue(int index, float defaultValue)
    {
        return 0;
    }

    @Override
    public String getIdAttribute()
    {
        return null;
    }

    @Override
    public String getClassAttribute()
    {
        return null;
    }

    @Override
    public int getIdAttributeResourceValue(int defaultValue)
    {
        return 0;
    }

    @Override
    public int getStyleAttribute()
    {
        return 0;
    }

    @Override
    public int getLineNumber()
    {
        return 0;
    }

    @Override
    public int getColumnNumber()
    {
        return 0;
    }

    @Override
    public boolean isWhitespace() throws XmlPullParserException
    {
        return false;
    }

    @Override
    public String getText()
    {
        return null;
    }

    @Override
    public char[] getTextCharacters(int[] holderForStartAndLength)
    {
        return new char[0];
    }

    @Override
    public String getNamespace()
    {
        return null;
    }

    @Override
    public String getName()
    {
        return null;
    }

    @Override
    public String getPrefix()
    {
        return null;
    }

    @Override
    public boolean isEmptyElementTag() throws XmlPullParserException
    {
        return false;
    }

    @Override
    public int getAttributeCount()
    {
        return 0;
    }

    @Override
    public String getAttributeNamespace(int index)
    {
        return null;
    }

    @Override
    public String getAttributeName(int index)
    {
        return null;
    }

    @Override
    public String getAttributePrefix(int index)
    {
        return null;
    }

    @Override
    public String getAttributeType(int index)
    {
        return null;
    }

    @Override
    public boolean isAttributeDefault(int index)
    {
        return false;
    }

    @Override
    public String getAttributeValue(int index)
    {
        return null;
    }

    @Override
    public String getAttributeValue(String namespace, String name)
    {
        return null;
    }

    @Override
    public int getEventType() throws XmlPullParserException
    {
        return 0;
    }

    @Override
    public int next() throws XmlPullParserException, IOException
    {
        return 0;
    }

    @Override
    public int nextToken() throws XmlPullParserException, IOException
    {
        return 0;
    }

    @Override
    public void require(int type, String namespace, String name) throws XmlPullParserException, IOException
    {

    }

    @Override
    public String nextText() throws XmlPullParserException, IOException
    {
        return null;
    }

    @Override
    public int nextTag() throws XmlPullParserException, IOException
    {
        return 0;
    }


    @Override
    public void close()
    {

    }
}
