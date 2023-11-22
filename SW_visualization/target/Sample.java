//package com.chansol.casino;

public abstract class Sample {
	public String any_field;
	private void sampleMethod() {
		Arm arm = new Arm();
		Robot robot = new Robot(new Arm());
	}
	private void mmm() {
		return 0;
	}
}

abstract class Parent{
}

interface Interface{
}

class Child extends Parent implements Interface{
}

class A{
	public void dependencyB() {
		B b = new B();
	}
	
	public void dependencyC(C c) {
		new C();
		if(true) {
			if(true) {
				if(true) {
					if(true) {
						if(true) {
							C c2 = new C();
						}
					}
				}
			}
		}
	}
	
	public B dependencyB() {
		return new B();
		if(true) {
			dependencyC(new C());
		}
	}
}

class B{
	private C c;
	private Vector<Vector<Vector<Vector<Vector<Vector<Vector<Vector<C>>>>>>>> c = new Vector<Vector<Vector<Vector<Vector<Vector<Vector<Vector<C>>>>>>>>();
}

class C{
	private B b;
	HashMap<Vector<B>> b1 = new HashMap<Vector<B>>();
}

class D{
	private C c;
}

class Human{
	public Human() {
		Arm arm = new Arm();
		Robot robot = new Robot(arm);
	}
}

class Robot{
	Arm arm;
	Leg leg;
	
	public Robot(Arm arm) {
		this.arm = arm;
		this.leg = new Leg();
	}
	
	public void boot(Arm arm) {
		this.arm = arm;
		this.leg = new Leg();
	}
}

class Arm{
}

class Leg{
}


/*
 *
 * package SW_visualization;
 * 
 * public class Sample {
 * 
 * }
 * 
 * abstract class Parent{ }
 * 
 * interface Interface{ }
 * 
 * class Child extends Parent implements Interface{ }
 * 
 * class A{ public void dependencyB() { B b = new B(); }
 * 
 * public void dependencyC(C c) { } }
 * 
 * class B{ private C c; }
 * 
 * class C{ private B b; }
 * 
 * class D{ private C c; }
 * 
 * class Human{ public Human() { Arm arm = new Arm(); Robot robot = new
 * Robot(arm); } }
 * 
 * class Robot{ Arm arm; Leg leg;
 * 
 * public Robot(Arm arm) { this.arm = arm; this.leg = new Leg(); } }
 * 
 * class Arm{ }
 * 
 * class Leg{ }
 */