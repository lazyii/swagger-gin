package io.swagger.v3.oas.models.links


import spock.lang.Specification
/**
 * Created by admin on 2021/1/13 10:19:35.
 */
class LinkParamterSpockTest extends Specification{

    def "testValue"() {
        given:
        LinkParameter linkParameter = new LinkParameter();
        linkParameter.setValue("foo");
        linkParameter.setValue("bar");
        linkParameter.setValue("baz");

        expect:
        linkParameter.value("bar") == linkParameter
        linkParameter.getValue() == "bar"
    }

    def "testEquals"() {
        given:
        LinkParameter linkParameter = new LinkParameter();

        expect:
        linkParameter !=null
        linkParameter !=new String()

        linkParameter == linkParameter
        linkParameter  == new LinkParameter()
    }

    def "testGetExtensions1"() {
        given:
        LinkParameter linkParameter = new LinkParameter();
        linkParameter.addExtension("", null);
        linkParameter.addExtension("y-", null);
        linkParameter.addExtension(null, null);

        expect:
        linkParameter.getExtensions() == null
    }

    def "testGetExtensions2"() {
        given:
        LinkParameter linkParameter = new LinkParameter();
        linkParameter.addExtension("x-", "foo");
        linkParameter.addExtension("x-", "bar");
        linkParameter.addExtension("x-", "baz");

        expect:
        linkParameter.getExtensions() ==
        new HashMap<String, Object>() {{
            put("x-", "baz");
        }}
    }

    def "testGetExtensions3"() {
        given:
        LinkParameter linkParameter = new LinkParameter();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("x-", "foo");
        hashMap.put("x-", "bar");
        hashMap.put("x-", "baz");
        linkParameter.setExtensions(hashMap);


        expect:
       linkParameter.getExtensions() ==
                new HashMap<String, Object>() {
                    {
                        put("x-", "baz");
                    }
                }
    }

    def "testExtensions"() {
        given:
        LinkParameter linkParameter = new LinkParameter();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("x-", "foo");
        hashMap.put("x-", "bar");
        hashMap.put("x-", "baz");

        expect:
        linkParameter.extensions(hashMap) == linkParameter
    }

    def "testToString"() {
        given:
        LinkParameter linkParameter = new LinkParameter();
        linkParameter.setValue("foo");

        expect:
        linkParameter.toString() == "class LinkParameter {\n}"

    }

}
