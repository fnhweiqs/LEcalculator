# LEcalculator
logical expression calculator

@Test
    public void regexTest(){
        String input = "((a OR !b) AND !(c OR d))";
        Map<String, Boolean> map = new HashMap<>() ;
        map.put("b", true) ;
        map.put("a", false) ;
        map.put("e", true) ;
        map.put("d", true) ;
        boolean res = false ;
        try {
            res = LECalculator.calLogicExp(input, map) ;
        } catch (LECalculator.IlegalLEException e) {
            System.out.println(e.toString());
        }
        System.out.println(res);
    }
