/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package demo.hw.client;

import javax.xml.ws.ProtocolException;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.hello_world_soap12_http.Greeter;
import org.apache.hello_world_soap12_http.PingMeFault;
import org.apache.hello_world_soap12_http.types.FaultDetail;

public final class Client {

    private Client() {
    } 

    public static void main(String args[]) throws Exception {
        
        if (args.length == 0) { 
            System.out.println("please specify wsdl");
            System.exit(1); 
        }
        
        System.out.println("WSDL URL: " + args[0]);
        JaxWsProxyFactoryBean clientFactory = new JaxWsProxyFactoryBean();
        clientFactory.setWsdlURL(args[0]);
        String address = "http://localhost:9000/SoapContext/SoapPort";
        clientFactory.setServiceClass(Greeter.class);
        clientFactory.setAddress(address);
        Greeter client = (Greeter) clientFactory.create();
        String resp; 

        System.out.println("Invoking sayHi...");
        resp = client.sayHi();
        System.out.println("Server responded with: " + resp);
        System.out.println();

        System.out.println("Invoking greetMe...");
        resp = client.greetMe(System.getProperty("user.name"));
        System.out.println("Server responded with: " + resp);
        System.out.println();

        System.out.println("Invoking greetMe with invalid length string, expecting exception...");
        try {
            resp = client.greetMe("Invoking greetMe with invalid length string, expecting exception...");
        } catch (ProtocolException e) {
            System.out.println("Expected exception has occurred: " + e.getClass().getName());
        }

        System.out.println();

        System.out.println("Invoking greetMeOneWay...");
        client.greetMeOneWay(System.getProperty("user.name"));
        System.out.println("No response from server as method is OneWay");
        System.out.println();

        try {
            System.out.println("Invoking pingMe, expecting exception...");
            client.pingMe();
        } catch (PingMeFault ex) {
            System.out.println("Expected exception: PingMeFault has occurred: " + ex.getMessage());
            FaultDetail detail = ex.getFaultInfo();
            System.out.println("FaultDetail major:" + detail.getMajor());
            System.out.println("FaultDetail minor:" + detail.getMinor());            
        }          
        System.exit(0); 
    }

}
