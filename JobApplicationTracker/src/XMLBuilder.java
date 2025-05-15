import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class XMLBuilder {
    String filepath = "jobs.xml";
    public List<Job> loadData(){
        List<Job> jobs = new ArrayList<>();
        try{
            File xmlFile = new File(filepath);
            if(!xmlFile.exists()) return jobs;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            NodeList nodeList = doc.getElementsByTagName("job");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element jobElement = (Element) nodeList.item(i);
                String companyName = jobElement.getElementsByTagName("company").item(0).getTextContent();
                String positionTitle = jobElement.getElementsByTagName("title").item(0).getTextContent();
                LocalDate date = LocalDate.parse(jobElement.getElementsByTagName("date").item(0).getTextContent());
                String status = jobElement.getElementsByTagName("status").item(0).getTextContent();
                boolean isRemote = Boolean.parseBoolean(jobElement.getElementsByTagName("isRemote").item(0).getTextContent());
                jobs.add(new Job(companyName,positionTitle, date,status,isRemote));

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return jobs;
    }
    public void Save(List<Job> jobs){
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("jobs");
            doc.appendChild(root);

            for(Job job : jobs){
                Element jobElement = doc.createElement("job");

                Element company = doc.createElement("company");
                company.appendChild(doc.createTextNode(job.getCompanyName()));
                jobElement.appendChild(company);

                Element title = doc.createElement("title");
                title.appendChild(doc.createTextNode(job.getPositionTitle()));
                jobElement.appendChild(title);

                Element date = doc.createElement("date");
                date.appendChild(doc.createTextNode(job.getApplicationDate().toString()));
                jobElement.appendChild(date);

                Element status = doc.createElement("status");
                status.appendChild(doc.createTextNode(job.getStatus()));
                jobElement.appendChild(status);

                Element isRemote = doc.createElement("isRemote");
                isRemote.appendChild(doc.createTextNode(String.valueOf(job.IsRemote())));
                jobElement.appendChild(isRemote);

                root.appendChild(jobElement);
            }
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT,"yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.transform(source, result);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
