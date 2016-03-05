package quiz;

import java.util.*;

public class StraightForwardGrading extends QuizObject {
		
	protected void endTest() {
		score = 0;
		
		for (int i = 0; i < correctAnswers.size(); i++) {
			if (correctAnswers.get(i) == 1)
				score++;
		}
		score = score*100 / numQuestions;
		System.out.println(score);
		System.out.println("Quiz started on " + startDate);
		System.out.println("Quiz ended on " + endDate);
	}

}
