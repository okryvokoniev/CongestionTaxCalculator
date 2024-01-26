- I assume it is ok to change a little bit date format
  from "2013-02-08 14:35:00" to "2013-02-08T14:35:00"
  to make it possible to deserialize requests by Spring Boot without additional configuration.
  Also, java.time.LocalDateTime class is newer then java.util.Date and
  provides many methods out of the box.

- it is not clear if all provided rules are applicable to other cities.
  if some rules should be possible to disable the code should be a little bit modified.

- probably need to add custom exceptions in case when configuration was not found. 
- it is good to add logging if this code will be used on production.

- it is not clear how to deal with dates like:
  "2013-02-08T06:20:27",
  "2013-02-08T06:27:00",
  "2013-02-08T07:21:00",
  "2013-02-08T07:27:00",
  "2013-02-08T08:05:00"
  do we need to compare the first with the second and the second compare with the third date
  or if we compare the second and the first then there is no need to compare the second and the third.
  I decided to group by first date plus 60 minutes.
  If the next date is added to the first group then it will not be added to the other group to avoid double
  taxation.
  So, from the input above I will get two groups:
  group 1:
  "2013-02-08T06:20:27",
  "2013-02-08T06:27:00"

  group 2:
  "2013-02-08T07:21:00",
  "2013-02-08T07:27:00",
  "2013-02-08T08:05:00"

- Is it ok to read the configuration one time when the application is started?
  I decided to read it one time and store the configuration in the hashmap, for the future improvement
  it is possible to add an API to rewrite the configuration during runtime.
