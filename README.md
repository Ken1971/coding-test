Hi. Since this is for the backend engineer coding test use, I will be writing this guide for people who are an expert in Java already.

Prerequisites:
This project uses Maven and Java 22. Please install the latest Maven and Java 22. Docker and MySQL are also required.
When downloaded for the first time, please change the mysql connection information inside the application.properties file and compose.yaml file.
Also, start the docker program before running this application.

Testing:
I use Postman for testing. It is an easy tool to use especially considering we have to upload a CSV file. 

Exposed EndPoints:

/create Creates a CSV file with random data for importing use.
        
        Required Params: filePath -> enter file path or simply file name here. If only the file name, it will be created under this project's main directory. example: test1000000.csv
                         rows -> enter the number of data rows to create here. Use 1000000 to create the required import file for task 5. example: 1000000

/import Import data to MySQL database using a CSV file. 
        
        Required Params: file -> In the Postman, instead of going to Params, go to the Body section. Select the form-data tab. Enter a new key with the name as file and select the type to be file. 
        Then in the value part, select "New file from local machine", and then pick that test1000000.csv file.

/getGameSales Get a list of game sales based on the input parameters. Since the date of sale is stored as a UNIX timestamp(to milliseconds). I allow search by date string or Unix timestamp.
        
        Search by Unix timestamp:
        Required Params: page -> page number for pagination use. example: 2
                         salePrice -> the sale price we are looking for. example: 30.1
                         moreThanPrice -> enter 1 if we are searching for greater than the sale price or 0 if less than the sale price. example: 1
                         fromDTS -> from date in the Unix timestamp. 1711929600000 for "2024-04-01 00:00:00". example: 1711929600000
                         toDTS -> to date in the Unix timestamp. 1714521599000 for "2024-04-30 23:59:59". example: 1714521599000

        Search by date in string format:
        Required Params: page -> page number for pagination use. example: 2
                         salePrice -> the sale price we are looking for. example: 30.1
                         moreThanPrice -> enter 1 if we are searching for greater than the sale price or 0 if less than the sale price. example: 1
                         fromDate -> from date in the format of "2024-04-01 00:00:00". example: 2024-04-01 00:00:00
                         toDate -> to date in the format of "2024-04-30 23:59:59". example: 2024-04-30 23:59:59

/getTotalSales Get the total number of games sold and sales generated during a given period. Also provided search by Unix timestamp or date strings.

        Search by Unix timestamp:
        Required Params: fromDTS -> from date in the Unix timestamp. 1711929600000 for "2024-04-01 00:00:00". example: 1711929600000
                         toDTS -> to date in the Unix timestamp. 1714521599000 for "2024-04-30 23:59:59". example: 1714521599000
        Optional Param: gameNo -> an integer value between 1 to 100. If provided, will search for results during a given period for that gameNo. example: 32

        Search by date in string format:
        Required Params: fromDate -> from date in the format of "2024-04-01 00:00:00". example: 2024-04-01 00:00:00
                         toDate -> to date in the format of "2024-04-30 23:59:59". example: 2024-04-30 23:59:59
        Optional Param: gameNo -> an integer value between 1 to 100. If provided, will search for results during a given period for that gameNo. example: 32

Run Steps:

1. Run the application inside intellij. 
2. Create a CSV file with 1000000 rows using the /create endpoint.
3. Import the file to the MYSQL using the /import endpoint.
4. Use /getGameSales endpoint to test for task 3.
5. Use /getTotalSales to test for task 4.

Note:
When running the application, the time used to do each endpoint task will be shown in the application console section (in milliseconds). Currently, the import process for 1 mil records is taking 90-100 seconds. 
Tried to fine-tune the batch process, but this is the best I can get to so far. Still working on that 30-second requirement... Task 2's tracking progress is still incomplete (because still working on the import process).




        
                      


