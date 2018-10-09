package money;

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
import com.sun.org.apache.xerces.internal.dom.TextImpl;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.cbr.web.GetCursOnDateXMLResponse.GetCursOnDateXMLResult;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.List;

public class GetCursOnDateResultParser {

    public static class Currency{
        public String name;
        public String chCode;
        public int code;
        public BigDecimal nom;
        public BigDecimal rate;

        public Currency(){

        }

        public Currency(String vname, String vchcode, int vcode, BigDecimal vnom, BigDecimal vcurs){
            this.name = vname;
            this.chCode = vchcode;
            this.code = vcode;
            this.nom = vnom;
            this.rate = vcurs;
        }
    }

    public static Currency getCurrencyByCurrencyCh(String curCh, GetCursOnDateXMLResult result) throws Exception{

        Currency answer = new Currency();

        List<Object> list = result.getContent();
        ElementNSImpl e = (ElementNSImpl) list.get(0);
        NodeList chCodeList = e.getElementsByTagName("VchCode");
        int length = chCodeList.getLength();

        boolean isFound = false;
        for (int i = 0; i< length; i++){
            if (isFound) break;

            Node curChNode = chCodeList.item(i);
            TextImpl textimpl = (TextImpl)curChNode.getFirstChild();
            String chVal = textimpl.getData();

            if (chVal.equalsIgnoreCase(curCh)){
                isFound = true;
                Node parent = curChNode.getParentNode();
                NodeList nodeList = parent.getChildNodes();
                parseNodeList(answer, nodeList);
            }
        }
        return answer;
    }

    private static void parseNodeList(Currency answer, NodeList nodeList){
        int paramLength = nodeList.getLength();
        for (int j=0; j<paramLength; j++){
            Node currentNode = nodeList.item(j);

            String name = currentNode.getNodeName();
            Node currentValue = currentNode.getFirstChild();
            String value = currentValue.getNodeValue();
            if (name.equalsIgnoreCase("Vname")){
                answer.name = value;
            }
            if (name.equalsIgnoreCase("Vnom")){
                answer.nom = new BigDecimal(value);
            }
            if (name.equalsIgnoreCase("Vcurs")){
                answer.rate = new BigDecimal(value);
            }
            if (name.equalsIgnoreCase("Vcode")){
                answer.code = Integer.parseInt(value);
            }
            if (name.equalsIgnoreCase("VchCode")){
                answer.chCode = value;
            }
        }
    }

    public static Currency getCurrencyByCurrencyCode(int curCode, GetCursOnDateXMLResult result) throws Exception{

        Currency answer = new Currency();

        List<Object> list = result.getContent();
        ElementNSImpl e = (ElementNSImpl) list.get(0);
        NodeList chCodeList =   e.getElementsByTagName("Vcode");
        int length = chCodeList.getLength();

        boolean isFound = false;
        for (int i = 0; i< length; i++){
            if (isFound) break;

            Node curChNode = chCodeList.item(i);
            TextImpl textimpl = (TextImpl)curChNode.getFirstChild();
            String chVal = textimpl.getData();

            if (chVal.equalsIgnoreCase(Integer.toString(curCode))){
                isFound = true;
                Node parent = curChNode.getParentNode();
                NodeList nodeList = parent.getChildNodes();
                parseNodeList(answer,nodeList);
            }
        }

        return answer;
    }
    public static XMLGregorianCalendar getXMLGregorianCalendarNow()
            throws DatatypeConfigurationException
    {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
        XMLGregorianCalendar now =
                datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
        return now;
    }

}
