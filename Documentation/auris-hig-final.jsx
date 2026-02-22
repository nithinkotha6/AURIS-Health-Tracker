import React, { useState } from 'react';
import {
  Home, CheckSquare, Plus, BarChart2, User,
  Droplet, Footprints, Mic, Moon, Activity,
  ChevronLeft, ChevronRight, Check, Circle,
  Coffee, Sun, Dumbbell, BookOpen, Flame,
  Eye, Dna, Leaf, Shield, Microscope, Bone, Zap, Atom
} from 'lucide-react';

export default function App() {
  const [activeTab, setActiveTab] = useState('home');

  return (
    <div className="min-h-screen bg-[#000000] flex items-center justify-center p-4 font-sans text-black overflow-hidden"
      style={{ fontFamily: "-apple-system, 'SF Pro Display', 'SF Pro Text', BlinkMacSystemFont, sans-serif", fontFeatureSettings: '"kern" 1, "liga" 1' }}>

      {/* Global Styles for Apple HIG & Liquid Glass */}
      <style dangerouslySetInnerHTML={{
        __html: `
        .hide-scrollbar::-webkit-scrollbar { display: none; }
        .hide-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }
        
        /* Apple Press State Micro-interaction */
        .ios-press:active {
          transform: scale(0.96);
          opacity: 0.8;
        }

        /* Standard iOS Drop Shadows */
        .ios-shadow-card {
          box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05), 0 1px 3px rgba(0, 0, 0, 0.03);
        }
        .ios-shadow-elevated {
          box-shadow: 0 4px 24px rgba(0, 0, 0, 0.08), 0 1px 4px rgba(0, 0, 0, 0.04);
        }
        
        /* Progress Bar Animation */
        .ios-progress {
          transition: stroke-dashoffset 1.2s cubic-bezier(0.4, 0, 0.2, 1), width 1.2s cubic-bezier(0.4, 0, 0.2, 1);
        }

        /* Staggered List Animation */
        @keyframes slideUpFade {
          from { opacity: 0; transform: translateY(8px); }
          to { opacity: 1; transform: translateY(0); }
        }
        .stagger-item { opacity: 0; animation: slideUpFade 0.4s cubic-bezier(0.4, 0, 0.2, 1) forwards; }
        .stagger-delay-1 { animation-delay: 50ms; }
        .stagger-delay-2 { animation-delay: 100ms; }
        .stagger-delay-3 { animation-delay: 150ms; }
        .stagger-delay-4 { animation-delay: 200ms; }

        /* Liquid Glass Tab Effect */
        .liquid-glass-tab {
          background: rgba(255, 255, 255, 0.35);
          backdrop-filter: blur(24px) saturate(200%);
          -webkit-backdrop-filter: blur(24px) saturate(200%);
          border: 1px solid rgba(255, 255, 255, 0.4);
          border-top: 1.5px solid rgba(255, 255, 255, 0.9);
          border-bottom: 1px solid rgba(255, 255, 255, 0.2);
          box-shadow: 
            0 16px 32px rgba(0, 0, 0, 0.08),
            inset 0 4px 10px rgba(255, 255, 255, 0.6),
            inset 0 -4px 10px rgba(255, 255, 255, 0.1);
          border-radius: 999px;
        }

        .liquid-glass-active {
          background: rgba(255, 255, 255, 0.45);
          box-shadow: 
            inset 0 2px 6px rgba(255, 255, 255, 0.8),
            0 4px 12px rgba(0, 0, 0, 0.05);
          border: 1px solid rgba(255, 255, 255, 0.4);
        }
      `}} />

      {/* Device Frame (375x812) */}
      <div className="relative w-[375px] h-[812px] bg-[#F2F2F7] rounded-[44px] overflow-hidden shadow-[0_20px_50px_rgba(0,0,0,0.15)] border-[8px] border-[#1C1C1E] flex flex-col">

        {/* Soft Ambient Light Orbs for Background depth */}
        <div className="absolute top-[-5%] left-[-10%] w-[250px] h-[250px] bg-[#BAE6FD] rounded-full mix-blend-multiply filter blur-[80px] opacity-[0.45] pointer-events-none fixed"></div>
        <div className="absolute bottom-[10%] right-[-10%] w-[300px] h-[300px] bg-[#bbf7d0] rounded-full mix-blend-multiply filter blur-[90px] opacity-[0.45] pointer-events-none fixed"></div>

        {/* Top Safe Area & Status Bar */}
        <div className="h-[44px] w-full shrink-0 flex items-end px-6 pb-2 z-50 pointer-events-none relative text-black">
          <div className="text-[15px] font-semibold tracking-tight">9:41</div>
          <div className="ml-auto flex gap-1.5 items-center">
            <div className="w-4 h-3 rounded-[3px] border-[1px] border-black relative opacity-90"><div className="absolute top-[1px] bottom-[1px] left-[1px] w-[9px] bg-black rounded-[1.5px]"></div></div>
            <div className="w-3.5 h-3.5 rounded-full border-[1.5px] border-black opacity-90"></div>
          </div>
        </div>

        {/* MAIN SCROLLABLE CONTENT */}
        <div className="flex-1 overflow-y-auto hide-scrollbar pb-[130px] relative z-10">

          {/* View Router */}
          <div className={`transition-opacity duration-300 ${activeTab === 'home' ? 'block' : 'hidden'}`}>
            <HomeView />
          </div>

          <div className={`transition-opacity duration-300 ${activeTab === 'habits' ? 'block' : 'hidden'}`}>
            <HabitsView />
          </div>

          <div className={`transition-opacity duration-300 ${activeTab === 'stats' ? 'block' : 'hidden'}`}>
            <StatsView />
          </div>

          {(activeTab !== 'home' && activeTab !== 'habits' && activeTab !== 'stats') && (
            <div className="flex-1 h-[600px] flex items-center justify-center text-[#3C3C4399] font-medium text-[15px]">
              {activeTab.charAt(0).toUpperCase() + activeTab.slice(1)} Screen (WIP)
            </div>
          )}

        </div>

        {/* ----- BOTTOM NAVIGATION (Liquid Glass Tab) ----- */}
        <div className="absolute bottom-[28px] left-1/2 -translate-x-1/2 w-[90%] h-[72px] z-50 flex items-center justify-between px-2 liquid-glass-tab">

          <div className="flex gap-1 w-[40%] justify-around">
            <NavItem icon={Home} label="Home" id="home" active={activeTab} set={setActiveTab} />
            <NavItem icon={CheckSquare} label="Habits" id="habits" active={activeTab} set={setActiveTab} />
          </div>

          {/* Center FAB */}
          <div className="relative -top-[16px] flex flex-col items-center">
            <button
              style={{ transition: 'transform 0.1s ease-out, opacity 0.1s ease-out' }}
              className="w-[56px] h-[56px] rounded-full bg-[#007AFF] flex items-center justify-center shadow-[0_8px_20px_rgba(0,122,255,0.40),inset_0_2px_4px_rgba(255,255,255,0.4)] ios-press border border-white/20"
            >
              <Plus size={28} className="text-white" strokeWidth={2.5} />
            </button>
            {/* Fixed label positioning below the floating pill */}
            <span className="text-[10px] font-medium text-[#3C3C4399] tracking-[0.1px] absolute -bottom-[16px]">Log</span>
          </div>

          <div className="flex gap-1 w-[40%] justify-around">
            <NavItem icon={BarChart2} label="Stats" id="stats" active={activeTab} set={setActiveTab} />
            <NavItem icon={User} label="Profile" id="profile" active={activeTab} set={setActiveTab} />
          </div>
        </div>

      </div>
    </div>
  );
}

// ==========================================
// VIEW: HOME 
// ==========================================
function HomeView() {
  const [timeFilter, setTimeFilter] = useState('Day');

  return (
    <div className="px-6 pt-10"> {/* Adjusted top padding for optimal iOS large title alignment */}

      {/* Top Section */}
      <div className="w-full flex flex-col mb-10 stagger-item stagger-delay-1">
        {/* Adjusted Greeting - lower placement, precise alignment */}
        <div className="w-full mb-5">
          <h1 className="text-[34px] font-bold text-black tracking-[-0.5px]">Hi, Mr. Kotha</h1>
        </div>

        {/* Perfected Pastel AI Bar based on reference image */}
        <div className="w-full relative rounded-full p-[2px] mb-8 ios-shadow-card"
          style={{ background: 'linear-gradient(90deg, #A7C7E7 0%, #D8B4E2 50%, #A8E6CF 100%)' }}>
          <div className="w-full h-[50px] bg-white/70 backdrop-blur-xl rounded-full flex items-center px-4 cursor-text ios-press">
            <Mic size={20} className="text-[#3C3C4399] mr-3 shrink-0" />
            <span className="text-[#3C3C4399] text-[15px] font-medium tracking-tight truncate">Ask AI – How's my protein intake this week?</span>
          </div>
        </div>

        {/* Central Stage: Trackers & Human Model */}
        <div className="relative w-full h-[380px] flex justify-between items-center shrink-0 px-1">

          <div className="flex flex-col items-center z-20 w-[64px] h-full justify-center gap-3">
            <div className="text-center flex flex-col items-center">
              <span className="text-[18px] font-bold text-black tracking-[-0.2px] leading-tight">2.5</span>
              <span className="text-[13px] font-medium text-[#3C3C4399] leading-tight mt-0.5">Liter</span>
            </div>
            <div className="w-[52px] h-[250px] bg-white/40 backdrop-blur-md rounded-[26px] flex items-end relative overflow-hidden ios-shadow-card border-[0.5px] border-white/60 p-1">
              <div className="w-full rounded-[22px] ios-progress flex justify-center pb-4 relative overflow-hidden shadow-sm"
                style={{ height: '75%', background: 'linear-gradient(180deg, #60A5FA 0%, #2563EB 100%)' }}>
                <div className="absolute top-0 right-1 w-[30%] h-full bg-white/20 rounded-full"></div>
                <Droplet size={20} className="text-white fill-white mt-auto z-10" />
              </div>
            </div>
            <div className="text-[16px] font-bold text-black mt-1">75%</div>
          </div>

          <div className="absolute left-1/2 -translate-x-1/2 w-[220px] h-[115%] flex justify-center items-center z-10 pointer-events-none mt-2">
            <svg viewBox="0 0 140 320" className="w-full h-full opacity-90 drop-shadow-md">
              <defs>
                <radialGradient id="headGlow" cx="40%" cy="30%" r="60%">
                  <stop offset="0%" stopColor="#FFFFFF" />
                  <stop offset="40%" stopColor="#E2E8F0" />
                  <stop offset="100%" stopColor="#94A3B8" />
                </radialGradient>
                <linearGradient id="bodyClay" x1="20%" y1="0%" x2="80%" y2="100%">
                  <stop offset="0%" stopColor="#F1F5F9" />
                  <stop offset="30%" stopColor="#E2E8F0" />
                  <stop offset="70%" stopColor="#CBD5E1" />
                  <stop offset="100%" stopColor="#94A3B8" />
                </linearGradient>
                <linearGradient id="rimLight" x1="0%" y1="0%" x2="100%" y2="0%">
                  <stop offset="0%" stopColor="rgba(255,255,255,0.9)" />
                  <stop offset="50%" stopColor="rgba(255,255,255,0)" />
                  <stop offset="100%" stopColor="rgba(255,255,255,0.4)" />
                </linearGradient>
              </defs>
              <g>
                <ellipse cx="70" cy="35" rx="14" ry="19" fill="url(#headGlow)" stroke="url(#rimLight)" strokeWidth="1" />
                <path d="M64 50 L64 60 L76 60 L76 50 Z" fill="url(#bodyClay)" />
                <path d="M52 65 C62 58 78 58 88 65 C96 75 96 90 92 105 C88 120 84 140 82 145 C78 150 62 150 58 145 C56 140 52 120 48 105 C44 90 44 75 52 65 Z" fill="url(#bodyClay)" stroke="url(#rimLight)" strokeWidth="1" />
                <path d="M52 65 C40 70 34 95 32 115 L26 160 C25 170 30 175 34 170 C38 160 42 125 46 105 Z" fill="url(#bodyClay)" stroke="url(#rimLight)" strokeWidth="1" />
                <path d="M88 65 C100 70 106 95 108 115 L114 160 C115 170 110 175 106 170 C102 160 98 125 94 105 Z" fill="url(#bodyClay)" stroke="url(#rimLight)" strokeWidth="1" />
                <path d="M26 160 C20 175 22 185 26 185 C30 185 34 175 34 170" fill="url(#bodyClay)" />
                <path d="M114 160 C120 175 118 185 114 185 C110 185 106 175 106 170" fill="url(#bodyClay)" />
                <path d="M58 145 C54 160 48 220 48 260 C46 275 48 285 54 285 C58 285 62 270 62 260 C62 220 68 165 70 148 Z" fill="url(#bodyClay)" stroke="url(#rimLight)" strokeWidth="1" />
                <path d="M82 145 C86 160 92 220 92 260 C94 275 92 285 86 285 C82 285 78 270 78 260 C78 220 72 165 70 148 Z" fill="url(#bodyClay)" stroke="url(#rimLight)" strokeWidth="1" />
                <path d="M48 280 C40 285 40 295 50 295 L58 295 C62 295 62 285 54 280 Z" fill="url(#bodyClay)" />
                <path d="M92 280 C100 285 100 295 90 295 L82 295 C78 295 78 285 86 280 Z" fill="url(#bodyClay)" />
              </g>
            </svg>
          </div>

          <div className="flex flex-col items-center z-20 w-[64px] h-full justify-center gap-3">
            <div className="text-center flex flex-col items-center">
              <span className="text-[18px] font-bold text-black tracking-[-0.2px] leading-tight">6500</span>
              <span className="text-[13px] font-medium text-[#3C3C4399] leading-tight mt-0.5">Steps</span>
            </div>
            <div className="w-[52px] h-[250px] bg-white/40 backdrop-blur-md rounded-[26px] flex items-end relative overflow-hidden ios-shadow-card border-[0.5px] border-white/60 p-1">
              <div className="w-full rounded-[22px] ios-progress flex justify-center pb-4 relative overflow-hidden shadow-sm"
                style={{ height: '65%', background: 'linear-gradient(180deg, #86EFAC 0%, #16A34A 100%)' }}>
                <div className="absolute top-0 right-1 w-[30%] h-full bg-white/20 rounded-full"></div>
                <Footprints size={20} className="text-[#064E3B] fill-[#064E3B] mt-auto z-10 opacity-80" />
              </div>
            </div>
            <div className="text-[16px] font-bold text-black mt-1">65%</div>
          </div>
        </div>
      </div>

      {/* Analytics Dashboard section */}
      <div className="w-full flex flex-col gap-4 stagger-item stagger-delay-2">

        {/* iOS Segmented Control Filter */}
        <div className="flex bg-[#7676801F] p-[2px] rounded-[8px] mb-2">
          {['Day', 'Week', 'Month', 'Year'].map(tab => (
            <button
              key={tab}
              onClick={() => setTimeFilter(tab)}
              className={`flex-1 py-1.5 rounded-[6px] text-[13px] font-semibold transition-all duration-150 ios-press ${timeFilter === tab ? 'bg-white shadow-[0_3px_8px_rgba(0,0,0,0.12)] text-black' : 'text-[#3C3C4399]'}`}
            >
              {tab}
            </button>
          ))}
        </div>

        <div className="grid grid-cols-2 gap-4">
          <div className="bg-white rounded-[20px] p-5 flex flex-col items-center justify-center aspect-square ios-shadow-card">
            <div className="relative w-[70px] h-[70px] flex items-center justify-center">
              <svg className="absolute inset-0 w-full h-full -rotate-90" viewBox="0 0 100 100">
                <circle cx="50" cy="50" r="42" fill="none" stroke="rgba(60,60,67,0.08)" strokeWidth="8" />
                <circle cx="50" cy="50" r="42" fill="none" stroke="url(#stepGrad)" strokeWidth="8" strokeLinecap="round" strokeDasharray="263.89" strokeDashoffset="92.36" className="ios-progress" />
                <defs>
                  <linearGradient id="stepGrad" x1="0" y1="0" x2="1" y2="1"><stop offset="0%" stopColor="#34C759" /><stop offset="100%" stopColor="#30D158" /></linearGradient>
                </defs>
              </svg>
              <Footprints size={24} className="text-[#34C759]" fill="currentColor" />
            </div>
            <div className="mt-4 text-center">
              <div className="text-[17px] font-bold text-black tracking-[-0.2px]">6500</div>
              <div className="text-[12px] font-medium text-[#3C3C4399]">Steps</div>
            </div>
          </div>

          <div className="bg-white rounded-[20px] p-5 flex flex-col items-center justify-center aspect-square ios-shadow-card">
            <div className="relative">
              <Moon size={32} className="text-[#AF52DE]" fill="currentColor" />
              <div className="absolute top-0 right-[-8px] text-[#AF52DE] text-[12px]">✨</div>
            </div>
            <div className="mt-5 text-center">
              <div className="text-[17px] font-bold text-black tracking-[-0.2px]">4h 55m</div>
              <div className="text-[12px] font-medium text-[#3C3C4399]">Sleep</div>
            </div>
          </div>
        </div>

        {/* Heart Rate Graph */}
        <div className="bg-white rounded-[20px] p-5 flex flex-col ios-shadow-card">
          <div className="flex justify-between items-start mb-4">
            <h3 className="text-[17px] font-semibold text-black tracking-[-0.2px]">Heart rate</h3>
            <div className="text-right">
              <div className="text-[15px] font-bold text-[#FF3B30]">131 <span className="text-[12px] font-medium text-[#3C3C4399]">high</span></div>
              <div className="text-[15px] font-bold text-black mt-0.5">65 <span className="text-[12px] font-medium text-[#3C3C4399]">avg</span></div>
            </div>
          </div>

          <div className="relative w-full h-[70px]">
            <div className="absolute inset-0 flex flex-col justify-between">
              <div className="border-b-[0.5px] border-[#3C3C431F] w-full h-0"></div>
              <div className="border-b-[0.5px] border-[#3C3C431F] w-full h-0"></div>
              <div className="border-b-[0.5px] border-[#3C3C431F] w-full h-0"></div>
            </div>
            <svg viewBox="0 0 300 80" className="w-full h-full relative z-10" preserveAspectRatio="none">
              <defs>
                <linearGradient id="hrGrad" x1="0" y1="0" x2="1" y2="0">
                  <stop offset="0%" stopColor="#FF3B30" />
                  <stop offset="100%" stopColor="#FF2D55" />
                </linearGradient>
              </defs>
              <path d="M 0 40 L 20 40 L 25 30 L 30 60 L 35 15 L 40 45 L 45 40 L 60 40 L 65 25 L 70 55 L 75 20 L 80 40 L 95 40 L 100 20 L 105 70 L 110 10 L 115 50 L 120 40 L 140 40 L 145 35 L 150 50 L 155 40 L 180 40"
                fill="none" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" stroke="url(#hrGrad)" />
            </svg>
          </div>

          <div className="flex justify-between mt-2 text-[10px] font-medium text-[#3C3C4399] px-1">
            <span>12 AM</span><span>4 AM</span><span>8 AM</span><span>12 PM</span><span>4 PM</span><span>8 PM</span><span>11 PM</span>
          </div>
        </div>

        {/* Weight & Readiness/Recovery */}
        <div className="grid grid-cols-[1fr_1.2fr] gap-4">
          <div className="bg-white rounded-[20px] p-5 flex flex-col justify-between relative ios-shadow-card">
            <div className="flex justify-between items-center">
              <span className="text-[15px] font-semibold text-black tracking-[-0.2px]">Weight</span>
              <div className="w-2.5 h-2.5 rounded-full bg-[#FF3B30]"></div>
            </div>
            <div className="text-[28px] font-bold text-black mt-4 mb-1 tracking-[-0.5px]">103 <span className="text-[17px] font-semibold text-[#3C3C4399]">kg</span></div>
          </div>

          <div className="bg-white rounded-[20px] p-5 flex justify-between relative ios-shadow-card">
            <div className="absolute left-1/2 top-5 bottom-5 w-[0.5px] bg-[#3C3C431F] -translate-x-1/2"></div>

            <div className="w-1/2 flex flex-col items-center justify-center relative pr-3">
              <span className="text-[12px] font-medium text-[#3C3C4399] mb-2">Readiness</span>
              <div className="relative w-[50px] h-[25px] overflow-hidden">
                <svg viewBox="0 0 100 50" className="w-full absolute bottom-0">
                  <path d="M 10 50 A 40 40 0 0 1 90 50" fill="none" stroke="rgba(60,60,67,0.08)" strokeWidth="8" strokeLinecap="round" />
                  <path d="M 10 50 A 40 40 0 0 1 90 50" fill="none" stroke="#FFCC00" strokeWidth="8" strokeLinecap="round"
                    strokeDasharray="125.6" strokeDashoffset="54" className="ios-progress" />
                </svg>
                <div className="absolute bottom-0 w-full text-center text-[15px] font-bold text-black">57</div>
              </div>
            </div>

            <div className="w-1/2 flex flex-col items-center justify-center relative pl-3">
              <span className="text-[12px] font-medium text-[#3C3C4399] mb-2">Recovery</span>
              <div className="relative w-[50px] h-[25px] overflow-hidden">
                <svg viewBox="0 0 100 50" className="w-full absolute bottom-0">
                  <path d="M 10 50 A 40 40 0 0 1 90 50" fill="none" stroke="rgba(60,60,67,0.08)" strokeWidth="8" strokeLinecap="round" />
                  <path d="M 10 50 A 40 40 0 0 1 90 50" fill="none" stroke="#34C759" strokeWidth="8" strokeLinecap="round"
                    strokeDasharray="125.6" strokeDashoffset="28" className="ios-progress" />
                </svg>
                <div className="absolute bottom-0 w-full text-center text-[15px] font-bold text-black">77</div>
              </div>
            </div>
          </div>
        </div>

        {/* Water Bar Thin Capsule */}
        <div className="flex flex-col gap-3">
          <div className="bg-white rounded-[16px] h-[52px] flex items-center px-4 relative overflow-hidden ios-shadow-card ios-press">
            <span className="text-[15px] font-semibold text-black tracking-[-0.2px] z-10 w-[60px]">Water</span>
            <div className="flex-1 h-[4px] bg-[#3C3C4314] rounded-full overflow-hidden mx-3 z-10">
              <div className="h-full rounded-full ios-progress" style={{ width: '70%', background: 'linear-gradient(90deg, #5AC8FA, #007AFF)' }}></div>
            </div>
            <span className="text-[15px] font-normal text-[#3C3C4399] z-10">3.5 L</span>
          </div>
        </div>

        {/* Calories & Macros Card */}
        <div className="bg-white rounded-[20px] p-5 flex gap-5 items-center mb-8 ios-shadow-card">

          <div className="relative w-[100px] h-[100px] flex items-center justify-center shrink-0">
            <svg className="absolute inset-0 w-full h-full -rotate-90" viewBox="0 0 100 100">
              <circle cx="50" cy="50" r="42" fill="none" stroke="rgba(60,60,67,0.08)" strokeWidth="8" />
              <circle cx="50" cy="50" r="42" fill="none" stroke="url(#calGrad)" strokeWidth="8"
                strokeLinecap="round" strokeDasharray="263.89" strokeDashoffset={263.89 * 0.45} className="ios-progress" />
              <defs>
                <linearGradient id="calGrad" x1="0" y1="0" x2="1" y2="1">
                  <stop offset="0%" stopColor="#FF9500" />
                  <stop offset="100%" stopColor="#FF6000" />
                </linearGradient>
              </defs>
            </svg>
            <div className="text-center flex flex-col items-center mt-0.5">
              <span className="text-[20px] font-bold text-black leading-none tracking-[-0.5px]">1,824</span>
              <span className="text-[11px] font-medium text-[#3C3C4399] mt-1 leading-none tracking-[0.1px]">kcal left</span>
            </div>
          </div>

          <div className="flex-1 flex flex-col justify-between">
            <div className="flex flex-col gap-3.5">
              <div>
                <div className="flex justify-between text-[12px] mb-1.5">
                  <span className="font-semibold text-black">Protein</span>
                  <span className="font-semibold text-black">82g <span className="text-[#3C3C4399] font-normal">/ 150g</span></span>
                </div>
                <div className="w-full h-[4px] bg-[#3C3C4314] rounded-full overflow-hidden">
                  <div className="h-full rounded-full ios-progress bg-[#007AFF]" style={{ width: '54%' }}></div>
                </div>
              </div>
              <div>
                <div className="flex justify-between text-[12px] mb-1.5">
                  <span className="font-semibold text-black">Carbs</span>
                  <span className="font-semibold text-black">210g <span className="text-[#3C3C4399] font-normal">/ 280g</span></span>
                </div>
                <div className="w-full h-[4px] bg-[#3C3C4314] rounded-full overflow-hidden">
                  <div className="h-full rounded-full ios-progress bg-[#34C759]" style={{ width: '75%' }}></div>
                </div>
              </div>
              <div>
                <div className="flex justify-between text-[12px] mb-1.5">
                  <span className="font-semibold text-black">Fat</span>
                  <span className="font-semibold text-black">38g <span className="text-[#3C3C4399] font-normal">/ 65g</span></span>
                </div>
                <div className="w-full h-[4px] bg-[#3C3C4314] rounded-full overflow-hidden">
                  <div className="h-full rounded-full ios-progress bg-[#FF9500]" style={{ width: '58%' }}></div>
                </div>
              </div>
            </div>

            <div className="flex justify-between items-center mt-4 pt-3 border-t-[0.5px] border-[#3C3C431F]">
              <div className="flex items-center gap-1.5">
                <Flame size={14} className="text-[#FF9500]" />
                <span className="text-[12px] font-semibold text-[#3C3C4399]">Streak</span>
              </div>
              <span className="text-[13px] font-semibold text-[#FF9500]">12 days</span>
            </div>
          </div>
        </div>

      </div>
    </div>
  );
}

// ==========================================
// VIEW: STATS / VITAMINS 
// ==========================================
function StatsView() {
  const vitaminsData = [
    { id: 1, name: 'Vitamin D', icon: Sun, percent: 22, amount: '220', unit: 'IU', isLow: true, color: '#FF9500' },
    { id: 2, name: 'Vitamin A', icon: Eye, percent: 83, amount: '697', unit: 'mcg', isLow: false, color: '#FF3B30' },
    { id: 3, name: 'Vitamin B12', icon: Dna, percent: 80, amount: '1.9', unit: 'mcg', isLow: false, color: '#AF52DE' },
    { id: 4, name: 'Vitamin C', icon: Leaf, percent: 80, amount: '72', unit: 'mg', isLow: false, color: '#34C759' },
    { id: 5, name: 'Vitamin E', icon: Shield, percent: 85, amount: '12.8', unit: 'mg', isLow: false, color: '#007AFF' },
    { id: 6, name: 'Biotin', icon: Microscope, percent: 76, amount: '22.8', unit: 'mcg', isLow: false, color: '#AF52DE' },
    { id: 7, name: 'Iron', icon: Droplet, percent: 78, amount: '14', unit: 'mg', isLow: false, color: '#FF3B30' },
    { id: 8, name: 'Calcium', icon: Bone, percent: 80, amount: '800', unit: 'mg', isLow: false, color: '#8E8E93' },
    { id: 9, name: 'Magnesium', icon: Zap, percent: 72, amount: '295', unit: 'mg', isLow: false, color: '#FF9500' },
    { id: 10, name: 'Zinc', icon: Atom, percent: 65, amount: '7.2', unit: 'mg', isLow: false, color: '#AF52DE' },
  ];

  return (
    <div className="px-6 pt-10"> {/* Adjusted top padding for optimal iOS large title alignment */}
      <div className="mb-4 mt-2 px-1 stagger-item stagger-delay-1">
        <h1 className="text-[34px] font-bold text-black tracking-[-0.5px] mb-0.5">Vitamins</h1>
        <p className="text-[15px] text-[#3C3C4399] font-normal">Today's Intake · Feb 20</p>
      </div>

      <div className="bg-white rounded-[20px] shadow-[0_1px_3px_rgba(0,0,0,0.04)] overflow-hidden flex flex-col mb-8 stagger-item stagger-delay-2">
        {vitaminsData.map((vitamin, index) => (
          <div key={vitamin.id} className="relative">
            <VitaminCard data={vitamin} />
            {index !== vitaminsData.length - 1 && (
              <div className="absolute bottom-0 right-0 left-[60px] border-b-[0.5px] border-[#3C3C431F]"></div>
            )}
          </div>
        ))}
      </div>

    </div>
  );
}

function VitaminCard({ data }) {
  const { name, icon: Icon, percent, amount, unit, isLow, color } = data;

  let valueColor = '#34C759';
  if (percent < 30) valueColor = '#FF9500';
  else if (percent < 60) valueColor = '#FF9500';

  return (
    <div className="px-4 py-3 flex flex-col gap-2.5 bg-white ios-press">
      <div className="flex justify-between items-center">
        <div className="flex items-center gap-3">
          <div className="w-[32px] h-[32px] rounded-[10px] flex items-center justify-center" style={{ backgroundColor: `${color}1F` }}>
            <Icon size={18} color={color} strokeWidth={2.5} />
          </div>
          <span className="text-[17px] font-semibold text-black tracking-[-0.2px]">{name}</span>
          {isLow && (
            <span className="text-[10px] font-bold text-[#FF3B30] bg-[#FF3B301F] px-1.5 py-0.5 rounded-[6px] ml-1 tracking-[0.1px]">
              LOW
            </span>
          )}
        </div>
        <div className="text-[15px] font-semibold" style={{ color: valueColor }}>
          {percent}% <span className="text-[#3C3C434D] font-normal ml-1">· {amount} {unit}</span>
        </div>
      </div>
      <div className="w-full h-[4px] bg-[#3C3C4314] rounded-full overflow-hidden ml-[44px]" style={{ width: 'calc(100% - 44px)' }}>
        <div
          className="h-full rounded-full ios-progress"
          style={{
            width: `${percent}%`,
            background: `linear-gradient(90deg, #FF9500 0%, #FFCC00 40%, #34C759 100%)`,
            backgroundSize: `${100 / (percent / 100)}% 100%`
          }}
        />
      </div>
    </div>
  );
}

// ==========================================
// VIEW: HABITS
// ==========================================
function HabitsView() {
  const [habits, setHabits] = useState([
    { id: 1, title: 'Drink Water', metric: '2 / 3 Liters', icon: Droplet, color: '#007AFF', category: 'Morning', done: true, streak: 12 },
    { id: 2, title: 'Morning Run', metric: '5 km', icon: Dumbbell, color: '#34C759', category: 'Morning', done: false, streak: 4 },
    { id: 3, title: 'Read Book', metric: '20 Pages', icon: BookOpen, color: '#AF52DE', category: 'Evening', done: true, streak: 21 },
    { id: 4, title: 'No Sugar', metric: 'All day', icon: Coffee, color: '#FF9500', category: 'All Day', done: false, streak: 1 },
  ]);

  const toggleHabit = (id) => {
    setHabits(habits.map(h => h.id === id ? { ...h, done: !h.done } : h));
  };

  const completedCount = habits.filter(h => h.done).length;
  const progressPercent = (completedCount / habits.length) * 100;

  return (
    <div className="px-6 pt-10"> {/* Adjusted top padding for optimal iOS large title alignment */}
      <div className="flex justify-between items-center mb-5 mt-2 px-1 stagger-item stagger-delay-1">
        <div>
          <h1 className="text-[34px] font-bold text-black tracking-[-0.5px]">Habits</h1>
          <p className="text-[15px] text-[#3C3C4399] font-normal mt-[-2px]">October 24, Thursday</p>
        </div>
        <div className="w-[36px] h-[36px] rounded-full bg-[#7676801F] flex items-center justify-center ios-press">
          <CheckSquare size={18} className="text-[#007AFF]" strokeWidth={2.5} />
        </div>
      </div>

      <div className="flex justify-between items-center mb-6 gap-1.5 stagger-item stagger-delay-2">
        {[{ d: 'M', n: 21 }, { d: 'T', n: 22 }, { d: 'W', n: 23 }, { d: 'T', n: 24, active: true }, { d: 'F', n: 25 }, { d: 'S', n: 26 }].map((day, i) => (
          <div key={i} className={`flex flex-col items-center justify-center w-[44px] h-[64px] rounded-[14px] transition-all ios-press ${day.active ? 'bg-[#007AFF]' : 'bg-white ios-shadow-card'}`}>
            <span className={`text-[12px] font-medium mb-1 ${day.active ? 'text-white/80' : 'text-[#3C3C4399]'}`}>{day.d}</span>
            <span className={`text-[17px] font-semibold ${day.active ? 'text-white' : 'text-black'}`}>{day.n}</span>
          </div>
        ))}
      </div>

      <div className="bg-white rounded-[20px] p-5 mb-6 flex items-center justify-between ios-shadow-card stagger-item stagger-delay-3">
        <div>
          <h2 className="text-[17px] font-semibold text-black tracking-[-0.2px] mb-0.5">Your Daily Goal</h2>
          <p className="text-[15px] text-[#3C3C4399] font-normal">Almost there! Keep it up.</p>
        </div>

        <div className="relative w-[60px] h-[60px] flex items-center justify-center shrink-0">
          <svg className="absolute inset-0 w-full h-full -rotate-90" viewBox="0 0 100 100">
            <circle cx="50" cy="50" r="42" fill="none" stroke="rgba(60,60,67,0.08)" strokeWidth="8" />
            <circle cx="50" cy="50" r="42" fill="none" stroke="url(#goalGrad)" strokeWidth="8"
              strokeLinecap="round" strokeDasharray="263.89" strokeDashoffset={263.89 - (263.89 * (progressPercent / 100))}
              className="ios-progress" />
            <defs>
              <linearGradient id="goalGrad" x1="0" y1="0" x2="1" y2="1">
                <stop offset="0%" stopColor="#34C759" />
                <stop offset="100%" stopColor="#30D158" />
              </linearGradient>
            </defs>
          </svg>
          <div className="text-center mt-0.5">
            <span className="text-[15px] font-bold text-black">{completedCount}</span>
            <span className="text-[12px] font-medium text-[#3C3C4399]">/{habits.length}</span>
          </div>
        </div>
      </div>

      <div className="flex flex-col gap-6 stagger-item stagger-delay-4">
        <div>
          <h3 className="text-[15px] font-semibold text-[#3C3C4399] tracking-[-0.2px] mb-2 px-1">Morning</h3>
          <div className="bg-white rounded-[20px] overflow-hidden ios-shadow-card flex flex-col">
            {habits.filter(h => h.category === 'Morning').map((habit, index, arr) => (
              <React.Fragment key={habit.id}>
                <HabitItem habit={habit} onToggle={() => toggleHabit(habit.id)} />
                {index !== arr.length - 1 && <div className="ml-[60px] border-b-[0.5px] border-[#3C3C431F]"></div>}
              </React.Fragment>
            ))}
          </div>
        </div>

        <div className="mb-6">
          <h3 className="text-[15px] font-semibold text-[#3C3C4399] tracking-[-0.2px] mb-2 px-1">Evening & All Day</h3>
          <div className="bg-white rounded-[20px] overflow-hidden ios-shadow-card flex flex-col">
            {habits.filter(h => h.category !== 'Morning').map((habit, index, arr) => (
              <React.Fragment key={habit.id}>
                <HabitItem habit={habit} onToggle={() => toggleHabit(habit.id)} />
                {index !== arr.length - 1 && <div className="ml-[60px] border-b-[0.5px] border-[#3C3C431F]"></div>}
              </React.Fragment>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}

function HabitItem({ habit, onToggle }) {
  const Icon = habit.icon;
  return (
    <div
      onClick={onToggle}
      className={`p-4 flex items-center gap-4 transition-colors duration-150 cursor-pointer ios-press ${habit.done ? 'bg-[#F2F2F7]/50' : 'bg-white'}`}
    >
      <div className="w-[36px] h-[36px] rounded-[10px] flex items-center justify-center shrink-0" style={{ backgroundColor: `${habit.color}1F` }}>
        <Icon size={20} color={habit.color} strokeWidth={2.5} />
      </div>

      <div className="flex-1">
        <h4 className={`text-[17px] font-normal tracking-[-0.2px] ${habit.done ? 'text-[#3C3C4399] line-through' : 'text-black'}`}>
          {habit.title}
        </h4>
        <div className="flex items-center gap-2 mt-0.5">
          <span className="text-[12px] font-medium text-[#3C3C4399] tracking-[0.1px]">{habit.metric}</span>
          {habit.streak > 0 && (
            <div className="flex items-center gap-0.5 bg-[#FF95001F] px-1.5 rounded-[4px]">
              <Flame size={10} color="#FF9500" />
              <span className="text-[10px] font-bold text-[#FF9500]">{habit.streak}</span>
            </div>
          )}
        </div>
      </div>

      <div className="shrink-0 px-2">
        <div className={`w-[24px] h-[24px] rounded-full flex items-center justify-center transition-all duration-150 ${habit.done ? 'bg-[#34C759]' : 'border-[1.5px] border-[#3C3C434D]'}`}>
          {habit.done && <Check size={14} className="text-white" strokeWidth={3} />}
        </div>
      </div>
    </div>
  );
}

// --- REUSABLE COMPONENTS ---

function NavItem({ icon: Icon, label, id, active, set }) {
  const isActive = active === id;
  return (
    <button
      onClick={() => set(id)}
      style={{ transition: 'transform 0.1s ease-out' }}
      className="flex flex-col items-center justify-center gap-[4px] w-[50px] relative z-10 ios-press"
    >
      <div className={`absolute top-[-6px] w-[44px] h-[44px] rounded-full transition-all duration-300 ${isActive ? 'liquid-glass-active' : 'bg-transparent opacity-0 scale-50'}`}></div>
      <Icon
        size={22}
        className={`relative z-10 transition-colors duration-150 ${isActive ? 'text-[#007AFF]' : 'text-[#3C3C4399]'}`}
        strokeWidth={isActive ? 2.5 : 2}
      />
      <span className={`text-[10px] font-medium transition-colors duration-150 tracking-[0.1px] relative z-10 ${isActive ? 'text-[#007AFF]' : 'text-[#3C3C4399]'}`}>
        {label}
      </span>
    </button>
  );
}