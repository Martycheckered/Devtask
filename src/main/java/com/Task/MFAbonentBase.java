package com.Task;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

public class MFAbonentBase{
    private static final Logger logger = Logger.getLogger(MFAbonentBase.class);

    public static void main(String[] args)  {
        //System.out.println (System.getProperty ("java.class.path"));

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      while (true) { // при неверном вводе имени файла (default) этот цикл перезапускает процесс ввода

          File file = chooseFileDirectory();

          if (file.exists()) {
              System.out.println("Файл с таким именем уже существует. " + "\n"+
                      "Введите 1, если хотите использовать имеющийся файл," +"\n"+
                      "Введите 2, чтобы перезаписать файл (после этого перезапустите программу c тем же именем файла)");
              int answer = 0;
              try {
                  answer = Integer.parseInt(reader.readLine());
                  if (answer==1) {
                      printFileOnScreen(file);
                      break;
                  }
                  else if (answer==2) {
                      createAbonentBase(file.getPath());// создание файла абон. базы с именем, введенным пользователем
                      printFileOnScreen(file);
                      break;
                  }
                  else System.out.println("Вы нажали неверную клавишу. Допускаются только ответы \"1\" и \"2\"");
              } catch (IOException e) {
                  System.out.println("Ошибка при считывании данных. Повторите." + "\n");
              }

             /* switch (answer){
                  case 1:  printFileOnScreen(file);
                      break;
                  case 2:   createAbonentBase(file.getPath());// создание файла абон. базы с именем, введенным пользователем
                      printFileOnScreen(file);
                      break;
                  default:
                      System.out.println("Вы нажали неверную клавишу. Допускаются только ответы \"1\" и \"2\"");
                      break;
              }
              break;*/

          } else {
              createAbonentBase(file.getPath());
              printFileOnScreen(file);
              break;
          }



}


    }




        //метод создания и записи в XML файл базы абонентов на основе DOM
        public static void createAbonentBase(String inputPath) {
            logger.trace("createAbonentBase");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            try {
                ArrayList<Abonent> AbList = new ArrayList<>(createAbonentList(getInputSizeOfList()));
                builder = factory.newDocumentBuilder();

                // создаем пустой объект Document, в котором будем
                // создавать наш xml-файл
                Document doc = builder.newDocument();
                // создаем корневой элемент
                Element rootElement =
                        doc.createElementNS("", "Abonents");
                // добавляем корневой элемент в объект Document
                doc.appendChild(rootElement);

                for (int i = 0; i < AbList.size(); i++) {
                    // добавляем 1-конечный дочерний элемент к корневому
                    rootElement.appendChild(getAbonent(doc, Integer.toString(i + 1), AbList.get(i).getTelephoneNumber(),
                            AbList.get(i).getBalance().toString()));
                }

                //создаем объект TransformerFactory для печати в консоль
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();

                // для красивого вывода в консоль
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource source = new DOMSource(doc);

                //печатаем в консоль или файл
                //StreamResult console = new StreamResult(System.out);
                StreamResult file = new StreamResult(new File(inputPath));

                //записываем данные
               // transformer.transform(source, console);

                transformer.transform(source, file);

                logger.trace("Создание XML файла закончено");

                logger.trace("createAbonentBase complete");

            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage(),e);
            }
        }

        // метод для создания нового узла XML-файла
        private static Node getAbonent (Document doc, String id, String number, String balance){
            Element abon = doc.createElement("Abonent");

            // устанавливаем атрибут id
            abon.setAttribute("id", id);

            // создаем элемент number
            abon.appendChild(getAbonentElements(doc, abon, "number", number));

            // создаем элемент balance
            abon.appendChild(getAbonentElements(doc, abon, "balance", balance));
            return abon;
        }

        // утилитный метод для создания нового узла XML-файла
        private static Node getAbonentElements (Document doc, Element element, String name, String value){
            Element node = doc.createElement(name);
            node.appendChild(doc.createTextNode(value));
            return node;
        }


        public static ArrayList<Abonent> createAbonentList(int abonAmount)  {
            logger.trace("createAbonentList");
            ArrayList<Abonent> abonentList = new ArrayList<>();
            //добавляем  абонентов
            //BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            //int amount = getInputSizeOfList();

            for (int i = 0; i < abonAmount; i++) {
                int tailOfNumber = (int) ((Math.random() * (9999999 - 1000000) + 1000000)); //рандомный хвост номера абонента из 7 цифр

                BigDecimal bal = new BigDecimal((Math.random() * 2001) - 1000); // рандомный баланс от -1000 до 1000
                BigDecimal balRounded = new BigDecimal(String.valueOf(bal.setScale(2, BigDecimal.ROUND_HALF_UP)));  //округляем до копеек


                abonentList.add(new Abonent("8999" + tailOfNumber, balRounded));
                logger.debug(abonentList.get(i).toString());

            }
            logger.trace("createAbonentList complete");
            return abonentList;
        }
    //получаем введенное число абонентов и обрабатываем его
    public  static int getInputSizeOfList()  {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int abonentsAmount;
        while (true){
            System.out.println("Введите ниже число абонентов, которое будет содержать база :");

            String rdrResult = null;
            try {
                rdrResult = reader.readLine();
                if (isPositiveInteger(rdrResult)){

                    try {
                        abonentsAmount = Integer.parseInt(rdrResult);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Ваш ответ не является положительным целым числом." +
                                " Пожалуйста, введите подходящее число (не более 2147483647).");
                    }

                }
                else  System.out.println("Ваш ответ не является положительным целым числом." +
                        " Пожалуйста, введите подходящее число (не более 2147483647).");
            } catch (IOException e) {
                //e.printStackTrace();
                System.out.println("Ошибка при считывании данных. Повторите." + "\n");
            }

        }
            return abonentsAmount;
    }

        // является ли строка числом
    public static boolean isPositiveInteger(String str)
    {
        return str.matches("^[1-9][0-9]+");
    }

    // создаем из узла документа объект Abonent
    private static Abonent getAbonentFromNode(Node node) {
        Abonent abon = new Abonent();    //испольуем конструктор по умолчанию
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            abon.setTelephoneNumber(getTagValue("number", element)); // установили телефонный номер

            String temp = getTagValue("balance", element); // получили строку баланса из элемента
            BigDecimal parsedBalance = new BigDecimal(temp); // прочитали её как BigDecimal

            abon.setBalance(parsedBalance);  //установили баланс
        }

        return abon;
    }

    // получаем значение элемента по указанному тегу
    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

    // разделение общего списка абонентов на 2: с + и -  балансом, а затем сортировка
    public static ArrayList [] splitByBalance (ArrayList<Abonent> listOfAbonents) {
            logger.trace("splitByBalance");
        ArrayList <Abonent>  minusList = new ArrayList<>();
        ArrayList <Abonent>  plusList = new ArrayList<>();
        BigDecimal zero = new BigDecimal(0); // BigDecimal вспомогательный, для цикла ниже

        for (Abonent abonent: listOfAbonents) {
            if (abonent.getBalance().compareTo(zero) < 0) {     // метод compareTo при сравнении с zero (= 0), если число
                minusList.add(abonent);                          // abonent.getBalance() меньше zero, возвращает -1
            } else plusList.add(abonent);
        }

        Collections.sort(minusList);
        Collections.sort(plusList);

        logger.trace("splitByBalance complete");
        return new ArrayList[]{minusList, plusList};
        }

    //проверка вводимого имени файла
    public static  boolean isFileNameCorrect (String inputPath) {
        logger.trace("isFileNameCorrect");
            Pattern pattern =Pattern.compile("^[a-z 0-9 _]+\\.xml$",Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputPath);
        logger.trace("isFileNameCorrect complete");
        return matcher.matches();

    }

    //вывод на экран содержимого файла
    public static void  printFileOnScreen ( File file) {
        logger.trace("printFileOnScreen");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(file); //теперь XML полностью загружен в память
            //   в виде объекта Document

            document.getDocumentElement().normalize();
            //получаем узлы с именем Abonent
            NodeList nodeList = document.getElementsByTagName("Abonent");

            // создадим из Document список объектов Abonent
            ArrayList<Abonent> listOfAbonents = new ArrayList<>();
            for (int i = 0; i <nodeList.getLength() ; i++) {
                listOfAbonents.add(getAbonentFromNode(nodeList.item(i)));
            }

            ArrayList [] BDresult = splitByBalance(listOfAbonents); // метод группировки абонентов,
            // возвращает массив с 2 списками внутри

            System.out.println("Абоненты с положительным балансом :");

            for (Object ab:  BDresult[1]) {
                Abonent ab1 =(Abonent) ab;
                System.out.println(ab1.toString());
            }
            System.out.println();
            System.out.println("Абоненты с отрицательным балансом :");
            for (Object ab: BDresult[0]) {
                Abonent ab1 =(Abonent) ab;
                System.out.println(ab1.toString());
            }
            logger.trace("printFileOnScreen complete");
        }
        catch (Exception e) {
            //exc.printStackTrace();
            logger.error(e.getMessage(),e);
            System.out.println("Извините, в работе приложения возникла ошибка");
        }
    }

     //где будем создавать файл
    public static File chooseFileDirectory() {
        File newFile = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true){

            System.out.println("Где желаете создать файл базы:" + "\n"+
                    "1 - В папке проекта " +"\n"+
                    "2 - В выбранной Вами папке" + "\n"+
                    "Введите цифру:");
            int pathChoose = 0;
            try {
                pathChoose = Integer.parseInt(reader.readLine());

                if (pathChoose ==1) {
                    newFile = createLocalFile();
                    break;
                }
                else if (pathChoose==2) {
                    newFile =createRemoteFile();
                    break;
                }
                else System.out.println("Ваш ответ некорректен. Введите число из вариантов выше.");
                /*switch (pathChoose) {
                    case 1:
                        newFile = createLocalFile();
                        break;
                    case 2:
                        newFile =createRemoteFile();
                        break;
                    default:  System.out.println("Ваш ответ некорректен. Введите число из вариантов выше.");

                }
                break;*/
            }

            catch (IOException e) {
                    System.out.println("Ваш ответ некорректен. Введите число из вариантов выше.");
            }

        }

     return newFile;

    }
     //создание файла в директории проекта
    public static File createLocalFile () {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        File newFile =null;
        while (true) {
            System.out.println("Напишите название файла в формате:  (имя файла).xml");
            System.out.println("Допустим только английский алфавит, цифры и символ подчеркивания");
            String ans = null;
            try {
                ans = reader.readLine();
                if (isFileNameCorrect(ans)) {
                    newFile = new File (ans);
                    break;
                }
                else {
                    System.out.println("Введенное имя некорректно. Попробуйте еще.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return newFile;
    }
    // создание файла в указанной папке
    public static File createRemoteFile () {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        File newFile =null;
        while (true) {
            System.out.println("Введите ниже директорию, в которой будет располагаться файл: ");
            File dirOfFolder = null;
            try {
                dirOfFolder = new File(reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert dirOfFolder != null;
            if (dirOfFolder.isDirectory()) {
                System.out.println("Напишите название файла в формате:  (имя файла).xml");
                System.out.println("Допустим только английский алфавит, цифры и символ подчеркивания");

                String fName = null;
                try {
                    fName = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert fName != null;
                newFile = new File(dirOfFolder,fName);
                break;
                           /* if (isFileNameCorrect(fName)) {
                                 newFile = new File(dirOfFolder,fName);
                                if (newFile.exists()) {
                                    System.out.println("Файл с таким именем уже существует. " + "\n"+
                                            "Введите 1, если хотите использовать имеющийся файл," +"\n"+
                                            "Введите 2, чтобы перезаписать файл (после этого перезапустите программу c тем же именем файла)");
                                    int answer =Integer.parseInt(reader.readLine());
                                    switch (answer){
                                        case 1:  printFileOnScreen(newFile);
                                            break;
                                        case 2:   createAbonentBase(fName);// создание файла абон. базы с именем, введенным пользователем
                                            printFileOnScreen(newFile);
                                            break;
                                        default:
                                            System.out.println("Вы нажали неверную клавишу. Допускаются только ответы \"1\" и \"2\"");
                                            break;
                                    }
                                    break;

                                } else {
                                    createAbonentBase(fName);
                                    printFileOnScreen(newFile);
                                    break;
                                }
                            }*/


            } else {
                System.out.println("Такая директория не найдена. Попробуйте ещё.");
            }
        }
        return newFile;
    }



}






