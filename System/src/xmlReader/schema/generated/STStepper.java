//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.04.18 at 11:01:48 AM IDT 
//


package xmlReader.schema.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}ST-Flows"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "stFlows"
})
@XmlRootElement(name = "ST-Stepper")
public class STStepper {

    @XmlElement(name = "ST-Flows", required = true)
    protected STFlows stFlows;

    /**
     * Gets the value of the stFlows property.
     * 
     * @return
     *     possible object is
     *     {@link STFlows }
     *     
     */
    public STFlows getSTFlows() {
        return stFlows;
    }

    /**
     * Sets the value of the stFlows property.
     * 
     * @param value
     *     allowed object is
     *     {@link STFlows }
     *     
     */
    public void setSTFlows(STFlows value) {
        this.stFlows = value;
    }

}
