package quiz;

import java.util.*;

public class MultiAnswerQuestion extends QuestionObject {

	public MultiAnswerQuestion(List<String> textToParse, StringBuilder answerGiven) {
		this.correctAnswer = new ArrayList<String>();
		this.question = textToParse.get(0).substring(2);
		for (int i = 1; textToParse.get(i).startsWith("A"); i++) {
			this.choices = choices + textToParse.get(i).substring(1) + "\n";
		}
		for (int i = 4; i < textToParse.get(textToParse.size()-1).length(); i = i + 3) {
			this.correctAnswer.add(textToParse.get(textToParse.size()-1).substring(i,i+1));
		}
		this.answerGiven = answerGiven;	
		this.numChoices = textToParse.size()-2;
	}
	
	protected int checkAnswer(List<String> answer) {

		if (answer.size() < correctAnswer.size())
			return -1;
		for (int i = 0; i < answer.size(); i++) {
			if (!correctAnswer.contains(answer.get(i))) {
				return -1;
			}
		}
		return 1;
	}
	
	public int promptUser() throws InvalidInput {
		Scanner userInput = new Scanner(System.in);
		System.out.println(question);
		System.out.println(choices);
		System.out.println("Pick the correct answer (choose which answers from 0 to " + (numChoices-1) + " may be correct; note that it's possible for only one question to be correct)");


		answerGiven.append(userInput.nextLine());
		List<String> answer = Arrays.asList(answerGiven.toString().split(" "));

		if (answerGiven.toString().equals(""))
			throw new InvalidInput("No Answer given");
		if (answer.size() > numChoices)
			throw new InvalidInput("Too many answers given");
		for (int i = 0; i < answer.size(); i++) {
			if (Integer.parseInt(answer.get(i)) >= numChoices || Integer.parseInt(answer.get(i)) < 0)
				throw new InvalidInput("Choose answers between 0 and " + (numChoices-1));
		}
		return checkAnswer(answer);
		

	}
}
