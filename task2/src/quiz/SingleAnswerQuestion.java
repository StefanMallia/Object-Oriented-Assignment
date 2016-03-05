package quiz;

import java.util.*;

public class SingleAnswerQuestion extends QuestionObject {

	
	public SingleAnswerQuestion(List<String> textToParse, StringBuilder answerGiven) {
			this.correctAnswer = new ArrayList<String>();
		this.question = textToParse.get(0).substring(2);
		for (int i = 1; textToParse.get(i).startsWith("A"); i++) {
			this.choices = choices + textToParse.get(i).substring(1) + "\n";
		}

		this.correctAnswer.add(textToParse.get(textToParse.size()-1).substring(4,5));
		this.answerGiven = answerGiven;		
		this.numChoices = textToParse.size()-2;
	}
	
	protected int checkAnswer(List<String> answer) {

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
		System.out.println("Pick the correct answer (from 0 to " + (numChoices-1) + ")");


		answerGiven.append(userInput.nextLine());
		List<String> answer = Arrays.asList(answerGiven.toString().split(" "));

		if (answerGiven.toString().equals(""))
			throw new InvalidInput("No Answer given");
		if (answer.size() > 1)
			throw new InvalidInput("Too many answers given");
		if (Integer.parseInt(answer.get(0)) >= numChoices || Integer.parseInt(answer.get(0)) < 0)
			throw new InvalidInput("Choose answers between 0 and " + (numChoices-1));
		return checkAnswer(answer);
		

	}
}
