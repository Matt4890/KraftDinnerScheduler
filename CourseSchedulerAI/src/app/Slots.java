public class Slots{
	
	private List<String> MonWedFriLectures;
    private List<String> TuThLectures;
    private List<String> MonTuWedThLabs;
    private List<String> FriLabs;

	
	private void generateCourseAndLabSlotTimes() {
			MonWedFriLectures = new ArrayList<>();
			TuThLectures = new ArrayList<>();
			MonTuWedThLabs = new ArrayList<>();
			FriLabs = new ArrayList<>();

			MonWedFriLectures.add("8:00");
			MonWedFriLectures.add("9:00");
			MonWedFriLectures.add("10:00");
			MonWedFriLectures.add("11:00");
			MonWedFriLectures.add("12:00");
			MonWedFriLectures.add("13:00");
			MonWedFriLectures.add("14:00");
			MonWedFriLectures.add("15:00");
			MonWedFriLectures.add("16:00");
			MonWedFriLectures.add("17:00");
			MonWedFriLectures.add("18:00");
			MonWedFriLectures.add("19:00");
			MonWedFriLectures.add("20:00");

			TuThLectures.add("8:00");
			TuThLectures.add("9:30");
			TuThLectures.add("11:00");
			TuThLectures.add("12:30");
			TuThLectures.add("14:00");
			TuThLectures.add("15:30");
			TuThLectures.add("17:00");
			TuThLectures.add("18:30");

			MonTuWedThLabs.add("8:00");
			MonTuWedThLabs.add("9:00");
			MonTuWedThLabs.add("10:00");
			MonTuWedThLabs.add("11:00");
			MonTuWedThLabs.add("12:00");
			MonTuWedThLabs.add("13:00");
			MonTuWedThLabs.add("14:00");
			MonTuWedThLabs.add("15:00");
			MonTuWedThLabs.add("16:00");
			MonTuWedThLabs.add("17:00");
			MonTuWedThLabs.add("18:00");
			MonTuWedThLabs.add("19:00");
			MonTuWedThLabs.add("20:00");

			FriLabs.add("8:00");
			FriLabs.add("10:00");
			FriLabs.add("12:00");
			FriLabs.add("14:00");
			FriLabs.add("16:00");
			FriLabs.add("18:00");
		}
}