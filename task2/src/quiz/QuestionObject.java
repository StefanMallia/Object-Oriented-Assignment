package quiz;

import java.util.*;

abstract class QuestionObject {
	protected String question;//question line
	protected int numChoices;//number of choices that are presented
	protected String choices = "";//choice lines are modified slightly and stored here to print
	protected List<String> correctAnswer;//in the case of singleAns and truthQues, this list would contain one element
	protected StringBuilder answerGiven = null;//to be assigned the StringBuilder that is passed by reference from QuizObject


	abstract int checkAnswer(List<String> answer);
	
	abstract int promptUser() throws InvalidInput;
		

}

