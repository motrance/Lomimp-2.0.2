package coffeemunchingmonkeys.tools.lomimp.stats;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/***
 *
 * Lomimp
 * stats.DateHelper
 * 
 * @version 2.0.2
 * @since 2026-05-15
 */
public class DateHelper {
    //Fields

   public DateHelper() {
   }

   /** 
    * @param daysToAdd
    * @return String
    */
   public String AddDays(int daysToAdd) {
      LocalDate futureDate = LocalDate.now().plusDays(daysToAdd);
      return futureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
   }

   /** 
    * @param daysToAdd
    * @param date
    * @return String
    */
   public String AddDays(int daysToAdd, LocalDate date) {
      if(date == null) {
         date = LocalDate.now();   
      }
      LocalDate futureDate = date.plusDays(daysToAdd);
      return futureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
   }

   /** 
    * @param daysToSubtract
    * @return String
    */
   public String SubtractDays(int daysToSubtract) {
      LocalDate pastDate = LocalDate.now().minusDays(daysToSubtract);
      return pastDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
   }

   /** 
    * @param daysToSubtract
    * @param date
    * @return String
    */
   public String SubtractDays(int daysToSubtract, LocalDate date) {
      if(date == null) {
         date = LocalDate.now();   
      }
      LocalDate pastDate = date.minusDays(daysToSubtract);
      return pastDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
   }

   
   /** 
    * @param WeeksToAdd
    * @return String
    */
   public String AddWeeks(int WeeksToAdd) {
      LocalDate futureDate = LocalDate.now().plusWeeks(WeeksToAdd);
      return futureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
   }

   /** 
    * @param WeeksToAdd
    * @param date
    * @return String
    */
   public String AddWeeks(int WeeksToAdd, LocalDate date) {
      if(date == null) {
         date = LocalDate.now();   
      }
      LocalDate futureDate = date.plusWeeks(WeeksToAdd);
      return futureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
   }

   /** 
    * @param WeeksToSubtract
    * @return String
    */
   public String SubtractWeeks(int WeeksToSubtract) {
      LocalDate pastDate = LocalDate.now().minusWeeks(WeeksToSubtract);
      return pastDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
   }
   
   /** 
    * @param WeeksToSubtract
    * @param date
    * @return String
    */
   public String SubtractWeeks(int WeeksToSubtract, LocalDate date) {
      if(date == null) {
         date = LocalDate.now();   
      }
      LocalDate pastDate = date.minusWeeks(WeeksToSubtract);
      return pastDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
   }

   /** 
    * @param monthsToAdd
    * @return String
    */
   public String AddMonths(int monthsToAdd) {
      LocalDate futureDate = LocalDate.now().plusMonths(monthsToAdd);
      return futureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
   }

   /** 
    * @param monthsToAdd
    * @param date
    * @return String
    */
   public String AddMonths(int monthsToAdd, LocalDate date) {
      if(date == null) {
         date = LocalDate.now();   
      }
      LocalDate futureDate = date.plusMonths(monthsToAdd);
      return futureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
   }

   /** 
    * @param monthsToSubtract
    * @return String
    */
   public String SubtractMonths(int monthsToSubtract) {
      LocalDate pastDate = LocalDate.now().minusMonths(monthsToSubtract);
      return pastDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
   }

   /** 
    * @param monthsToSubtract
    * @param date
    * @return String
    */
   public String SubtractMonths(int monthsToSubtract, LocalDate date) {
      if(date == null) {
         date = LocalDate.now();   
      }
      LocalDate pastDate = date.minusMonths(monthsToSubtract);
      return pastDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
   }

   /** 
    * @param yearsToAdd
    * @return String
    */
   public String AddYears(int yearsToAdd) {
      LocalDate futureDate = LocalDate.now().plusYears(yearsToAdd);
      return futureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
   }

   /** 
    * @param yearsToAdd
    * @param date
    * @return String
    */
   public String AddYears(int yearsToAdd, LocalDate date) {
      if(date == null) {
         date = LocalDate.now();   
      }
      LocalDate futureDate = date.plusYears(yearsToAdd);
      return futureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
   }

   /** 
    * @param yearsToSubtract
    * @return String
    */
   public String SubtractYears(int yearsToSubtract) {
      LocalDate pastDate = LocalDate.now().minusYears(yearsToSubtract);
      return pastDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
   }

   /** 
    * @param yearsToSubtract
    * @param date
    * @return String
    */
   public String SubtractYears(int yearsToSubtract, LocalDate date) {
      if(date == null) {
         date = LocalDate.now();   
      }
      LocalDate pastDate = date.minusYears(yearsToSubtract);
      return pastDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
   }
}
